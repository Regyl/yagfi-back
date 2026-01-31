package com.github.regyl.gfi.service.impl.other;

import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.source.IssueSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.auto-upload.enabled", havingValue = "true")
public class IssueLoaderServiceImpl implements ScheduledService {

    @Qualifier("issueLoadAsyncExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;
    private final Collection<IssueSourceService> sourceServices;

    private final JdbcTemplate jdbcTemplate;
    private final CacheManager cacheManager;

    @Override
    @Scheduled(fixedRateString = "${spring.properties.auto-upload.period-mills}", initialDelay = 1000)
    public void schedule() {
        IssueTables table = determineTable();
        sourceServices.forEach(service -> service.upload(table));

        while (true) {
            LockSupport.parkNanos(Duration.ofMinutes(1).toNanos());
            if (taskExecutor.getQueueSize() == 0) {
                break;
            }

            //FIXME set timeout
        }

        log.info("Issue pulling finished (but maybe not all uploaded to DB yet)");

        replaceView(table);
    }

    private IssueTables determineTable() {
        Long countFirst = jdbcTemplate.queryForObject("select count(*) from gfi." + IssueTables.FIRST.getIssueTableName(), Long.class);

        if (countFirst == null || countFirst == 0) {
            return IssueTables.FIRST;
        } else  {
            return IssueTables.SECOND;
        }
    }

    private void replaceView(IssueTables table) {
        jdbcTemplate.execute("CREATE OR REPLACE VIEW issue_v as select * from gfi." + table.getIssueTableName());
        jdbcTemplate.execute("CREATE OR REPLACE VIEW repository_v as select * from gfi." + table.getRepoTableName());

        log.info("Views recreated");

        IssueTables expiredTable = IssueTables.getDifferent(table);
        jdbcTemplate.execute("TRUNCATE TABLE gfi." + expiredTable.getIssueTableName());
        jdbcTemplate.execute(String.format("TRUNCATE TABLE gfi.%s CASCADE", expiredTable.getRepoTableName()));

        log.info("Expired tables truncated");

        jdbcTemplate.execute(String.format("alter sequence gfi.%s_id_seq restart", expiredTable.getIssueTableName()));
        jdbcTemplate.execute(String.format("alter sequence gfi.%s_id_seq restart", expiredTable.getRepoTableName()));
        log.info("Sequences restarted");

        cacheManager.getCacheNames().forEach(cacheName -> cacheManager.getCache(cacheName).clear());
        log.info("Caches evicted");
    }
}

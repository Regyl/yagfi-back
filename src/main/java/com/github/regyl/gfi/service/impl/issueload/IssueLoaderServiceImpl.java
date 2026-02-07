package com.github.regyl.gfi.service.impl.issueload;

import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feature-enabled.auto-upload", havingValue = "true")
public class IssueLoaderServiceImpl implements ScheduledService {

    private final Collection<IssueSourceService> sourceServices;

    private final JdbcTemplate jdbcTemplate;
    private final CacheManager cacheManager;

    @Override
    @Scheduled(fixedRateString = "${spring.properties.auto-upload.period-mills}", initialDelay = 1000)
    public void schedule() {
        log.info("Start issue load task");
        IssueTables table = determineTable();
        Collection<CompletableFuture<Void>> futures = sourceServices.stream()
                .flatMap(service -> service.upload(table).stream())
                .toList();

        //waiting all issues to be done
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        sourceServices.forEach(IssueSourceService::raiseUploadEvent);

        replaceView(table);
        log.info("Issue load finished");
    }

    private IssueTables determineTable() {
        Long countFirst = jdbcTemplate.queryForObject(
                "select count(*) from gfi." + IssueTables.FIRST.getIssueTableName(),
                Long.class
        );

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

        cacheManager.getCacheNames().forEach(cacheName -> {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
        log.info("Caches evicted");
    }
}

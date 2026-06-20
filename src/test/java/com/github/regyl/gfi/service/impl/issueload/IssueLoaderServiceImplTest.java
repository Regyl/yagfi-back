package com.github.regyl.gfi.service.impl.issueload;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DefaultUnitTest
class IssueLoaderServiceImplTest {

    @Test
    void shouldSkipWhenIssueLoadAlreadyRunning() {
        IssueSourceService sourceService = Mockito.mock(IssueSourceService.class);
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        CacheManager cacheManager = Mockito.mock(CacheManager.class);
        IssueLoadingLockService lockService = new IssueLoadingLockService();
        lockService.tryAcquireIssueLoadLock();

        IssueLoaderServiceImpl target = new IssueLoaderServiceImpl(
                List.of(sourceService),
                lockService,
                jdbcTemplate,
                cacheManager
        );

        target.schedule();

        verifyNoInteractions(sourceService, jdbcTemplate);
    }

    @Test
    void shouldReleaseLockWhenIssueLoadFails() {
        IssueSourceService sourceService = Mockito.mock(IssueSourceService.class);
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        CacheManager cacheManager = Mockito.mock(CacheManager.class);
        IssueLoadingLockService lockService = new IssueLoadingLockService();
        IssueLoaderServiceImpl target = new IssueLoaderServiceImpl(
                List.of(sourceService),
                lockService,
                jdbcTemplate,
                cacheManager
        );

        when(jdbcTemplate.queryForObject(Mockito.anyString(), eq(Long.class))).thenReturn(0L);
        when(sourceService.upload(eq(IssueTables.FIRST)))
                .thenReturn(List.of(CompletableFuture.failedFuture(new RuntimeException("boom"))));

        assertThatThrownBy(target::schedule).isInstanceOf(CompletionException.class);
        assertThat(lockService.isIssueLoadInProgress()).isFalse();
    }
}

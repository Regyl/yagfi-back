package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.impl.issueload.IssueLoaderServiceImpl;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@DefaultUnitTest
class IssueLoaderServiceImplTest {

    private IssueLoaderServiceImpl target;

    @Mock
    private IssueSourceService sourceService;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        target = new IssueLoaderServiceImpl(
                List.of(sourceService), jdbcTemplate, cacheManager, new CountDownLatch(0)
        );
    }

    @Test
    void testSchedule_uploadsAndRaisesEvent() {
        when(sourceService.upload(any(IssueTables.class)))
                .thenReturn(List.of(CompletableFuture.completedFuture(null)));
        when(cacheManager.getCacheNames()).thenReturn(List.of());

        target.schedule();

        verify(sourceService).upload(any(IssueTables.class));
        verify(sourceService).raiseUploadEvent();
    }
}

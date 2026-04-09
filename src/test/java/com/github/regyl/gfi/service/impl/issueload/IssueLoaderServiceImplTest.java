package com.github.regyl.gfi.service.impl.issueload;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DefaultUnitTest
public class IssueLoaderServiceImplTest {

    @Mock
    private Collection<IssueSourceService> sourceServices;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private LoadLocker loadLocker;

    @InjectMocks
    private IssueLoaderServiceImpl issueLoaderService;

    @Test
    void issueLoaderServiceSchedulePauseTest() {
        when(loadLocker.getLoadingInfo()).thenReturn(true);
        issueLoaderService.schedule();

        verify(loadLocker).getLoadingInfo();

        verifyNoMoreInteractions(sourceServices, jdbcTemplate, cacheManager);

    }

    @Test
    void shouldExecuteUploadFlowWhenNotLoading() {
        when(loadLocker.getLoadingInfo()).thenReturn(false);
        IssueSourceService service = mock(IssueSourceService.class);
        when(sourceServices.stream()).thenReturn(java.util.stream.Stream.of(service));
        when(service.upload(any())).thenReturn(java.util.List.of(CompletableFuture.completedFuture(null)));

        issueLoaderService.schedule();

        verify(loadLocker).getLoadingInfo();
        verify(service).upload(any());
        verify(service, atMostOnce()).raiseUploadEvent();
    }
}

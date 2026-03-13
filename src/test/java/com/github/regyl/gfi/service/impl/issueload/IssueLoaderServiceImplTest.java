package com.github.regyl.gfi.service.impl.issueload;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.cache.CacheManager;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collection;

import static org.mockito.Mockito.*;

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
    void issueLoaderServiceScheduleWorkTest() {
        when(loadLocker.getLoadingInfo()).thenReturn(false);
        issueLoaderService.schedule();

        verify(loadLocker).getLoadingInfo();
    }
}

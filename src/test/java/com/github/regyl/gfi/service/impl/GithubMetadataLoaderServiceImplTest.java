package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.model.MetadataRequestModel;
import com.github.regyl.gfi.repository.GitHubMetadataRepository;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.github.regyl.gfi.service.impl.issueload.IssueLoadingLockService;
import com.github.regyl.gfi.service.other.LabelService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.function.Supplier;

import static org.mockito.Mockito.verifyNoInteractions;

@DefaultUnitTest
class GithubMetadataLoaderServiceImplTest {

    @Test
    void shouldSkipWhenIssueLoadInProgress() {
        GithubClientService<MetadataRequestModel, Integer> githubClient = Mockito.mock(GithubClientService.class);
        LabelService labelService = Mockito.mock(LabelService.class);
        GitHubMetadataRepository metadataRepository = Mockito.mock(GitHubMetadataRepository.class);
        Supplier<LocalDate> scrappingStartDate = () -> LocalDate.of(2025, 1, 1);
        IssueLoadingLockService lockService = new IssueLoadingLockService();
        lockService.tryAcquireIssueLoadLock();

        GithubMetadataLoaderServiceImpl target = new GithubMetadataLoaderServiceImpl(
                githubClient,
                labelService,
                scrappingStartDate,
                metadataRepository,
                lockService
        );

        target.schedule();

        verifyNoInteractions(githubClient, labelService, metadataRepository);
    }
}

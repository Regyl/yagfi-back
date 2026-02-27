package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.model.MetadataRequestModel;
import com.github.regyl.gfi.repository.GitHubMetadataRepository;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.github.regyl.gfi.service.other.LabelService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

import static org.mockito.Mockito.when;

@DefaultUnitTest
class GithubMetadataLoaderServiceImplTest {

    private GithubMetadataLoaderServiceImpl target;

    @Mock
    private GithubClientService<MetadataRequestModel, Integer> githubClient;
    @Mock
    private LabelService labelService;
    @Mock
    private GitHubMetadataRepository metadataRepository;

    private CountDownLatch metadataLatch;

    private final Supplier<LocalDate> scrappingStartDate = () -> LocalDate.of(2024, 1, 1);

    @BeforeEach
    void setUp() {
        metadataLatch = new CountDownLatch(1);
        target = new GithubMetadataLoaderServiceImpl(
                githubClient, labelService, scrappingStartDate, metadataRepository, metadataLatch
        );
    }

    @Test
    void testSchedule_latchReleasedAfterExecution() {
        when(labelService.findAll()).thenReturn(List.of());

        target.schedule();

        Assertions.assertThat(metadataLatch.getCount()).isZero();
    }

    @Test
    void testSchedule_latchReleasedEvenOnError() {
        when(labelService.findAll()).thenThrow(new RuntimeException("error"));

        Assertions.assertThatThrownBy(() -> target.schedule())
                .isInstanceOf(RuntimeException.class);

        Assertions.assertThat(metadataLatch.getCount()).isZero();
    }
}

package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.entity.GitHubMetadataEntity;
import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.model.MetadataRequestModel;
import com.github.regyl.gfi.repository.GitHubMetadataRepository;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.github.regyl.gfi.service.other.LabelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;


@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feature-enabled.auto-upload-meta", havingValue = "true")
public class GithubMetadataLoaderServiceImpl implements ScheduledService {

    private final GithubClientService<MetadataRequestModel, Integer> githubClient;
    private final LabelService labelService;
    @Qualifier("scrappingStartDate")
    private final Supplier<LocalDate> scrappingStartDate;
    private final GitHubMetadataRepository metadataRepository;
    private final CountDownLatch metadataLatch;

    @Async
    @Scheduled(fixedRate = 604_800_000, initialDelay = 1_000)
    public void schedule() {
        try {
            String dateFilter = scrappingStartDate.get().toString();
            log.info("Start collecting GitHub metadata for labels from {}", dateFilter);
            Collection<LabelModel> labels = labelService.findAll();

            Collection<GitHubMetadataEntity> entities = new ArrayList<>();
            for (LabelModel labelModel : labels) {
                String label = labelModel.getTitle();
                MetadataRequestModel model = new MetadataRequestModel(label, dateFilter);
                int totalCount = githubClient.execute(model);
                log.info("Found {} GitHub issues for label {}", totalCount, label);
                entities.add(new GitHubMetadataEntity(label, totalCount));
            }

            metadataRepository.saveAll(entities);
            log.info("Finished collecting GitHub metadata for labels");
        } finally {
            metadataLatch.countDown();
        }
    }
}

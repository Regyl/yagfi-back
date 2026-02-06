package com.github.regyl.gfi.service.impl.issueload;

import com.github.regyl.gfi.controller.dto.github.rest.GithubSearchResponseDto;
import com.github.regyl.gfi.entity.GitHubMetadataEntity;
import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.repository.GitHubMetadataRepository;
import com.github.regyl.gfi.service.ScheduledService;
import com.github.regyl.gfi.service.other.LabelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feature-enabled.auto-upload-meta", havingValue = "true")
public class GithubMetadataLoaderServiceImpl implements ScheduledService {

    @Qualifier("githubRestClient")
    private final RestClient githubRestClient;
    private final LabelService labelService;
    private final GitHubMetadataRepository metadataRepository;

    /**
     * <p>
     * Without LockSupport#parkNanos in a loop can fail due to
     * <a href="https://docs.github.com/en/rest/using-the-rest-api/rate-limits-for-the-
     * rest-api?apiVersion=2022-11-28#about-secondary-rate-limits">GitHub secondary limits</a>
     */
    @Async
    @Override
    @Scheduled(fixedRate = 604_800_000, initialDelay = 1_000) //1 week
    public void schedule() {
        log.info("Start collecting GitHub metadata for labels");
        Collection<LabelModel> labels = labelService.findAll();
        String dateFilter = LocalDate.now().minusYears(1).toString();

        Collection<GitHubMetadataEntity> entities = new ArrayList<>();
        for (LabelModel labelModel : labels) {
            String label = labelModel.getTitle();
            int totalCount = getIssueCountForLabel(label, dateFilter);
            entities.add(new GitHubMetadataEntity(label, totalCount));

            LockSupport.parkNanos(Duration.ofSeconds(30).toNanos());
        }

        metadataRepository.saveAll(entities);
        log.info("Finished collecting GitHub metadata for labels");
    }

    private int getIssueCountForLabel(String label, String dateFilter) {
        String query = String.format("is:issue is:open no:assignee label:\"%s\" created:>=%s", label, dateFilter);
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = String.format("https://api.github.com/search/issues?q=%s&per_page=1", encodedQuery);
        URI uri = URI.create(url);
        
        try {
            GithubSearchResponseDto response = githubRestClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GithubSearchResponseDto.class);
            if (response == null) {
                log.warn("Empty response for label '{}'", label);
                return 0;
            }

            return response.getTotalCount();
        } catch (Exception e) {
            log.error("Error getting issue count for label '{}'", label, e);
            return 0;
        }
    }
}

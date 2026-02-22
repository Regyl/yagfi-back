package com.github.regyl.gfi.service.impl.github;

import com.github.regyl.gfi.dto.github.rest.GithubSearchResponseDto;
import com.github.regyl.gfi.exception.RateLimitExceedException;
import com.github.regyl.gfi.model.MetadataRequestModel;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feature-enabled.auto-upload-meta", havingValue = "true")
public class GithubIssueCountClientServiceImpl implements GithubClientService<MetadataRequestModel, Integer> {

    @Qualifier("githubRestClient")
    private final RestClient githubRestClient;
    @Qualifier("githubMetaRateLimiter")
    private final RateLimiter rateLimiter;

    @Override
    public Integer execute(MetadataRequestModel rq) {
        String label = rq.getLabel();
        String dateFilter = rq.getDateFilter();
        String query = String.format("is:issue is:open no:assignee label:\"%s\" created:>=%s", label, dateFilter);
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String url = String.format("https://api.github.com/search/issues?q=%s&per_page=1", encodedQuery);
        URI uri = URI.create(url);

        try {
            rateLimiter.acquire();
            GithubSearchResponseDto response = githubRestClient.get()
                    .uri(uri)
                    .retrieve()
                    .body(GithubSearchResponseDto.class);
            if (response == null) {
                log.warn("Empty response for label '{}'", label);
                return 0;
            }

            return response.getTotalCount();
        } catch (HttpClientErrorException.Forbidden e) {
            //https://docs.github.com/graphql/overview/rate-limits-and-node-limits-for-the-graphql-api#secondary-rate-limits
            log.error("Exceeded a secondary rate limit: {}", e.getMessage());
            throw new RateLimitExceedException();
        } catch (Exception e) {
            log.error("Error getting issue count for label '{}'", label, e);
            return 0;
        }
    }
}

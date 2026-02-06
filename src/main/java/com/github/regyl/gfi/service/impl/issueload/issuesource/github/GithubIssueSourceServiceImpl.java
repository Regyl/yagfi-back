package com.github.regyl.gfi.service.impl.issueload.issuesource.github;

import com.github.regyl.gfi.controller.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.controller.dto.request.IssueRequestDto;
import com.github.regyl.gfi.model.IssueSources;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.model.event.IssueSyncCompletedEvent;
import com.github.regyl.gfi.service.issueload.GithubQueryBuilderService;
import com.github.regyl.gfi.service.issueload.IssueSourceService;
import com.github.regyl.gfi.service.other.DataService;
import com.github.regyl.gfi.service.other.LabelService;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubIssueSourceServiceImpl implements IssueSourceService {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-issue-request.graphql");

    private final GraphQlClient githubClient;
    private final LabelService labelService;
    private final DataService dataService;
    private final ApplicationEventPublisher eventPublisher;
    @Qualifier("issueLoadAsyncExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;
    private final GithubQueryBuilderService queryBuilderService;

    @Override
    public void raiseUploadEvent() {
        eventPublisher.publishEvent(new IssueSyncCompletedEvent(IssueSources.GITHUB, OffsetDateTime.now()));
        log.info("All github issues synced successfully");
    }

    @Override
    public void upload(IssueTables table) {
        Collection<String> queries = labelService.findAll().stream()
                .flatMap(label -> queryBuilderService.apply(label).stream())
                .toList();
        log.info("Created {} different queries", queries.size());

        for (String query : queries) {

            taskExecutor.submit(() -> {
                try {
                    IssueDataDto response = getIssues(new IssueRequestDto(query, null));
                    dataService.save(response, table);

                    boolean hasNextPage = response.hasNextPage();
                    while (hasNextPage) {
                        response = getIssues(new IssueRequestDto(query, response.getEndCursor()));
                        dataService.save(response, table);
                        hasNextPage = response.hasNextPage();
                    }
                } catch (HttpClientErrorException.Forbidden e) {
                    //https://docs.github.com/graphql/overview/rate-limits-and-node-limits-for-the-graphql-api#secondary-rate-limits
                    log.error("Exceeded a secondary rate limit: {}", e.getMessage());
                    LockSupport.parkNanos(Duration.ofSeconds(30).toNanos());
                } catch (Exception e) {
                    log.error("Error uploading issues for query: {}", query, e);
                }
            });
        }
    }

    private IssueDataDto getIssues(IssueRequestDto dto) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("query", dto.getQuery());
        variables.put("cursor", dto.getCursor());
        ClientGraphQlResponse clientGraphQlResponse = githubClient.document(QUERY)
                .variables(variables)
                .executeSync();
        if (!clientGraphQlResponse.isValid()) {
            log.error("graph ql response is invalid");
        }
        return clientGraphQlResponse.toEntity(IssueDataDto.class);
    }
}

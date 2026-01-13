package com.github.regyl.gfi.service.impl.source;

import com.github.regyl.gfi.controller.dto.github.IssueData;
import com.github.regyl.gfi.model.IssueRequestDto;
import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.service.DataService;
import com.github.regyl.gfi.service.label.LabelService;
import com.github.regyl.gfi.service.source.IssueSourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubIssueSourceServiceImpl implements IssueSourceService {

    private static final String QUERY = """
            query SearchIssuesByLabel($query: String!, $cursor: String) {
                              rateLimit {
                                cost
                                remaining
                                resetAt
                              }
                              search(query: $query, type: ISSUE, first: 100, after: $cursor) {
                                pageInfo {
                                  hasNextPage
                                  endCursor
                                }
                                nodes {
                                  ... on Issue {
                                    id
                                    number
                                    title
                                    url
                                    state
                                    updatedAt
                                    createdAt
                                    repository {
                                        id
                                        nameWithOwner
                                        url
                                        description
                                        stargazerCount
                                        primaryLanguage {
                                            id
                                            name
                                        }
                                        updatedAt
                                    }
                                    labels(first: 20) {
                                        nodes {
                                            name
                                        }
                                    }
                                  }
                                }
                              }
                            }
            """;

    private final GraphQlClient githubClient;
    private final LabelService labelService;
    private final DataService dataService;
    private final ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void upload() {
        Collection<LabelModel> labels = labelService.findAll();
        for (LabelModel label : labels) {

            String query = String.format("is:issue is:open no:assignee label:\"%s\"", label.getTitle());
            taskExecutor.submit(() -> {
                IssueData response = getIssues(new IssueRequestDto(query, null));
                dataService.save(response);

                String cursor = response.getEndCursor();
                while (StringUtils.isNotBlank(cursor)) { //FIXME supply as new task to taskExecutor
                    response = getIssues(new IssueRequestDto(query, cursor));
                    dataService.save(response);
                    cursor = response.getEndCursor();
                }
            });
        }
    }

    private IssueData getIssues(IssueRequestDto dto) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("query", dto.getQuery());
        variables.put("cursor", dto.getCursor());
        ClientGraphQlResponse clientGraphQlResponse = githubClient.document(QUERY)
                .variables(variables)
                .executeSync();
        if (!clientGraphQlResponse.isValid()) {
            log.error("graph ql response is invalid");
        }
        return clientGraphQlResponse.toEntity(IssueData.class);
    }
}

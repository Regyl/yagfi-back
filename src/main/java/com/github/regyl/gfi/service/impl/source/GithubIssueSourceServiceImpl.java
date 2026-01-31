package com.github.regyl.gfi.service.impl.source;

import com.github.regyl.gfi.controller.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.controller.dto.request.IssueRequestDto;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.service.other.DataService;
import com.github.regyl.gfi.service.other.LabelService;
import com.github.regyl.gfi.service.source.IssueSourceService;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-issue-request.graphql");

    private final GraphQlClient githubClient;
    private final LabelService labelService;
    private final DataService dataService;
    @Qualifier("issueLoadAsyncExecutor")
    private final ThreadPoolTaskExecutor taskExecutor;

    @Override
    public void upload(IssueTables table) {
        Collection<LabelModel> labels = labelService.findAll();
        for (LabelModel label : labels) {

            if (log.isDebugEnabled()) {
                log.info("Uploading label {}", label);
            }

            String query = String.format("is:issue is:open no:assignee label:\"%s\"", label.getTitle());
            taskExecutor.submit(() -> {
                IssueDataDto response = getIssues(new IssueRequestDto(query, null));
                dataService.save(response, table);

                String cursor = response.getEndCursor();
                while (StringUtils.isNotBlank(cursor)) {
                    response = getIssues(new IssueRequestDto(query, cursor));
                    dataService.save(response, table);
                    cursor = response.getEndCursor();
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

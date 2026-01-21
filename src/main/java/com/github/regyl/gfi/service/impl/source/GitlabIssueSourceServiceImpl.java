package com.github.regyl.gfi.service.impl.source;

import com.github.regyl.gfi.controller.dto.gitlab.GitlabIssueData;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.service.DataService;
import com.github.regyl.gfi.service.label.LabelService;
import com.github.regyl.gfi.service.source.IssueSourceService;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.auto-upload.gitlab", havingValue = "true")
public class GitlabIssueSourceServiceImpl implements IssueSourceService {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/gitlab-request.graphql");

    @Qualifier("gitlabClient")
    private final GraphQlClient gitlabClient;
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

            taskExecutor.submit(() -> {
                GitlabIssueData response = getIssues(Collections.singleton(label.getTitle()), null);
                dataService.save(response, table);

                String cursor = response.getEndCursor();
                while (StringUtils.isNotBlank(cursor)) {
                    response = getIssues(Collections.singleton(label.getTitle()), cursor);
                    dataService.save(response, table);
                    cursor = response.getEndCursor();
                }
            });
        }
    }

    private GitlabIssueData getIssues(Collection<String> labels, String cursor) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("labels", labels);
        variables.put("after", cursor);

        try {
            ClientGraphQlResponse clientGraphQlResponse = gitlabClient.document(QUERY)
                    .variables(variables)
                    .executeSync();
            if (!clientGraphQlResponse.isValid()) {
                log.error("GitLab GraphQL response is invalid: {}", clientGraphQlResponse.getErrors());
                return null;
            }
            return clientGraphQlResponse.toEntity(GitlabIssueData.class);
        } catch (Exception e) {
            log.error("graph ql response is invalid", e);
            return null;
        }
    }
}

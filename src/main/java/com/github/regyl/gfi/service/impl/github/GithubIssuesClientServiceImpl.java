package com.github.regyl.gfi.service.impl.github;

import com.github.regyl.gfi.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GithubIssuesClientServiceImpl extends AbstractGraphQlGithubClientService<IssueRequestDto, IssueDataDto> {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-issue-request.graphql");

    @Override
    protected String getQuery() {
        return QUERY;
    }

    @Override
    protected Class<IssueDataDto> getReturnType() {
        return IssueDataDto.class;
    }

    @Override
    protected Map<String, Object> toVariables(IssueRequestDto rq) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("query", rq.getQuery());
        variables.put("cursor", rq.getCursor());
        return variables;
    }
}

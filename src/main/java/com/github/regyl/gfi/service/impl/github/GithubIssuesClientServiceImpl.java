package com.github.regyl.gfi.service.impl.github;

import com.github.regyl.gfi.controller.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.controller.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.util.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;

import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Slf4j
@Component
public class GithubIssuesClientServiceImpl extends AbstractGraphQlGithubClientService<IssueRequestDto, IssueDataDto> {

    private static final String QUERY = ResourceUtil.getFilePayload("graphql/github-issue-request.graphql");

    private final Retry retry;
    
    public GithubIssuesClientServiceImpl() {
        RetryConfig config = RetryConfig.custom().maxAttempts(5).waitDuration(Duration.ofMillis(500))
                .retryOnException(e -> {
                    log.error("Retrying due to Bad Gateway exception: {}", e.getMessage());
                    return e instanceof HttpServerErrorException.BadGateway;
                }).build();
        this.retry = Retry.of("githubIssueRetry", config);
    }
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
   
    @Override
    public IssueDataDto execute(IssueRequestDto rq) {
        Supplier<IssueDataDto> decorated = Retry.decorateSupplier(retry, () -> super.execute(rq));
        return decorated.get();
    }

}
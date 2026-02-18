package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.controller.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.controller.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.service.github.GithubClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubRetryService {

    private final GithubClientService<IssueRequestDto, IssueDataDto> githubClient;

    @Retryable(includes = {
            HttpServerErrorException.BadGateway.class }, maxRetries = 4, delayString = "500ms", multiplier = 1.5)
    public IssueDataDto fetchWithRetry(IssueRequestDto task) {
        log.info("Fetching issues for query : {}", task.getQuery());
        return githubClient.execute(task);
    }

}

package com.github.regyl.gfi.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import com.github.regyl.gfi.controller.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.controller.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.service.github.GithubClientService;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubRetryService {
	// private static int counter = 0;

	private final GithubClientService<IssueRequestDto, IssueDataDto> githubClient;

	@Retryable(includes = {
			HttpServerErrorException.BadGateway.class }, maxRetries = 4, delayString = "500ms", multiplier = 1.5)
	public IssueDataDto fetchWithRetry(IssueRequestDto task) {

		/*
		 * counter++;
		 * 
		 * if (counter < 3) { log.info("Simulating BadGateway attempt {}", counter);
		 * throw HttpServerErrorException.create( HttpStatus.BAD_GATEWAY,
		 * "Simulated 502", null, null, null ); }
		 */

		// log.info("Success on attempt {}", counter);
		log.info("Fetching issues for query: {}", task.getQuery());
		return githubClient.execute(task);
	}

}

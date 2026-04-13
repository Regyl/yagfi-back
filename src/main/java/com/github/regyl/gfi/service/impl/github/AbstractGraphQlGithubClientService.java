package com.github.regyl.gfi.service.impl.github;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.graphql.client.ClientGraphQlResponse;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.github.regyl.gfi.exception.RateLimitExceedException;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.google.common.util.concurrent.RateLimiter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractGraphQlGithubClientService<T, S> implements GithubClientService<T, S> {

    @Autowired
    @Qualifier("githubRateLimiter")
    private RateLimiter rateLimiter;

    @Autowired
    private GraphQlClient githubClient;

    @Override
    public S execute(T rq) {
        Map<String, Object> variables = toVariables(rq);

        int maxRetries = 3;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                rateLimiter.acquire();
                return run0(variables);

            } catch (HttpClientErrorException.Forbidden e) {
                log.error("Exceeded a secondary rate limit: {}", e.getMessage());
                throw new RateLimitExceedException();

            } catch (HttpServerErrorException.BadGateway e) {
                attempt++;
                log.warn("Bad Gateway error, retrying... attempt {}/{}", attempt, maxRetries);

                if (attempt >= maxRetries) {
                    log.error("Max retries reached. Failing request.");
                    return null;
                }

                try {
                    Thread.sleep(2000); // wait before retry
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            } catch (Exception e) {
                log.error("Error fetching github GraphQL response for params: {}", variables, e);
                return null;
            }
        }

        return null;
    }

    protected abstract Map<String, Object> toVariables(T rq);

    protected abstract String getQuery();

    protected abstract Class<S> getReturnType();

    private S run0(Map<String, Object> variables) {
        String query = getQuery();

        ClientGraphQlResponse clientGraphQlResponse = githubClient.document(query)
                .variables(variables)
                .executeSync();

        if (!clientGraphQlResponse.isValid()) {
            log.error("graph ql response is invalid");
        }

        return clientGraphQlResponse.toEntity(getReturnType());
    }
}
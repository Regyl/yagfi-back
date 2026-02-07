package com.github.regyl.gfi.configuration.github;

import com.github.regyl.gfi.configuration.async.AutoUploadConfigurationProperties;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.function.Supplier;

@Configuration
public class GitHubConfigurationFactory {

    @Bean
    public GraphQlClient githubClient(GithubConfigurationProperties configProps) {
        String authHeaderValue = String.format("Bearer %s", configProps.getToken());
        RestClient restClient = RestClient.create("https://api.github.com/graphql");
        return HttpSyncGraphQlClient.create(restClient)
                .mutate()
                .header("Authorization", authHeaderValue)
                .build();
    }

    @Bean
    public RestClient githubRestClient(GithubConfigurationProperties configProps) {
        String authHeaderValue = String.format("Bearer %s", configProps.getToken());
        return RestClient.builder()
                .defaultHeader("Authorization", authHeaderValue)
                .defaultHeader("Accept", "application/vnd.github.v3+json")
                .build();
    }

    @Bean("githubRateLimiter")
    public RateLimiter githubRateLimiter(AutoUploadConfigurationProperties configProps) {
        return RateLimiter.create(configProps.getQueryPerSecond());
    }

    @Bean("githubMetaRateLimiter")
    @ConditionalOnProperty(value = "spring.properties.feature-enabled.auto-upload-meta", havingValue = "true")
    public RateLimiter githubMetaRateLimiter() {
        return RateLimiter.create(0.0167d); //1 query per 60 seconds
    }

    @Bean("scrappingStartDate")
    public Supplier<LocalDate> scrappingStartDate(Supplier<LocalDate> dateSupplier) {
        return () -> dateSupplier.get().minusMonths(6);
    }
}

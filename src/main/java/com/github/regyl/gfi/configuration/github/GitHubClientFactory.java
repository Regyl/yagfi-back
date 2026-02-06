package com.github.regyl.gfi.configuration.github;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

@Configuration
public class GitHubClientFactory {

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
}

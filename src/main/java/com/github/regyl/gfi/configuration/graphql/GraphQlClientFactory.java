package com.github.regyl.gfi.configuration.graphql;

import com.github.regyl.gfi.configuration.graphql.github.GithubConfigurationProperties;
import com.github.regyl.gfi.configuration.graphql.gitlab.GitlabConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpSyncGraphQlClient;
import org.springframework.web.client.RestClient;

@Configuration
public class GraphQlClientFactory {

    /**
     * HttpSyncGraphQlClient uses RestClient to execute GraphQL requests over HTTP through a blocking transport contract and chain of interceptors.
     */
    @Bean
    public GraphQlClient githubClient(GithubConfigurationProperties configProps) {
        String authHeaderValue = String.format("Bearer %s", configProps.getToken());
        RestClient restClient = RestClient.create("https://api.github.com/graphql"); //FIXME move to configuration props
        return HttpSyncGraphQlClient.create(restClient)
                .mutate()
                .header("Authorization", authHeaderValue)
                .build();
    }

    @Bean
    public GraphQlClient gitlabClient(GitlabConfigurationProperties configProps) {
        String authHeaderValue = String.format("Bearer %s", configProps.getToken());
        RestClient restClient = RestClient.create(configProps.getUrl());
        return HttpSyncGraphQlClient.create(restClient)
                .mutate()
                .header("Authorization", authHeaderValue)
                .build();
    }
}

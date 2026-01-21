package com.github.regyl.gfi.configuration.graphql.gitlab;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.properties.gitlab")
public class GitlabConfigurationProperties {

    private String token;
    private String url;
}

package com.github.regyl.gfi.configuration.graphql.github;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.properties.github")
public class GithubConfigurationProperties {

    private String token;
}

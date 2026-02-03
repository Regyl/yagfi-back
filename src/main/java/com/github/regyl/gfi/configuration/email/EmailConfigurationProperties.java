package com.github.regyl.gfi.configuration.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.properties.email")
public class EmailConfigurationProperties {

    private String username;
    private String password;
}

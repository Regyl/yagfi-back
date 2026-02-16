package com.github.regyl.gfi.configuration.ipinfo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.properties.ipinfo")
public class IpInfoConfigurationProperties {

    private String token;

    private int cacheSize;
}

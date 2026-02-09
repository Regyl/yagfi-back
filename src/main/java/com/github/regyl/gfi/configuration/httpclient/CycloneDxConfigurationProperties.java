package com.github.regyl.gfi.configuration.httpclient;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(value = "spring.properties.cyclonedx")
public class CycloneDxConfigurationProperties {

    private List<String> services;
}

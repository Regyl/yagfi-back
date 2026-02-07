package com.github.regyl.gfi.configuration.async;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(value = "spring.properties.auto-upload")
public class AutoUploadConfigurationProperties {

    private Integer corePoolSize;
    private Integer queryPerSecond;
}

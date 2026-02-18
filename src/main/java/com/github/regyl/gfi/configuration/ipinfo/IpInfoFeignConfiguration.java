package com.github.regyl.gfi.configuration.ipinfo;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;

@RequiredArgsConstructor
public class IpInfoFeignConfiguration {

    private final IpInfoConfigurationProperties ipInfoConfig;

    @Bean
    public RequestInterceptor ipInfoRequestInterceptor() {
        return requestTemplate -> {
            String authToken = "Bearer " + ipInfoConfig.getToken();
            requestTemplate.header("Authorization", authToken);
        };
    }
}

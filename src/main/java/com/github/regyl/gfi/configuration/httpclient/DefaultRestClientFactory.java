package com.github.regyl.gfi.configuration.httpclient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestClient;

@Configuration
public class DefaultRestClientFactory {

    @Bean
    @Primary
    public RestClient defaultRestClient() {
        return RestClient.create();
    }
}

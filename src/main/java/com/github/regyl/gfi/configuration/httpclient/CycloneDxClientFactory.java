package com.github.regyl.gfi.configuration.httpclient;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "spring.properties.feature-enabled.feed-generation", havingValue = "true")
public class CycloneDxClientFactory {

    @Bean
    public SbomHttpClientResponseHandlerImpl sbomResponseHandler() {
        return new SbomHttpClientResponseHandlerImpl();
    }

    @Bean
    public HealthHttpClientResponseHandlerImpl healthResponseHandler() {
        return new HealthHttpClientResponseHandlerImpl();
    }

    @Bean
    public Collection<HttpHost> cycloneDxHosts(CycloneDxConfigurationProperties configProps) {
        String template = "http://";
        return configProps.getServices().stream()
                .map(service -> template + service)
                .map(host -> {
                    try {
                        return HttpHost.create(host);
                    } catch (URISyntaxException e) {
                        log.error(e.getMessage(), e);
                        return null;
                    }
                }).filter(Objects::nonNull)
                .toList();
    }

    @Bean("cdxgenHealthClient")
    public CloseableHttpClient cdxgenHealthClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10, TimeUnit.SECONDS)
                .setResponseTimeout(5, TimeUnit.SECONDS)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Bean("cdxgenSbomClient")
    public CloseableHttpClient cdxgenSbomClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(10, TimeUnit.SECONDS)
                .setResponseTimeout(15, TimeUnit.MINUTES)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}

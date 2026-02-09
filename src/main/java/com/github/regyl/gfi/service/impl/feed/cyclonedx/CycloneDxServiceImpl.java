package com.github.regyl.gfi.service.impl.feed.cyclonedx;

import com.github.regyl.gfi.controller.dto.cyclonedx.health.HealthResponseDto;
import com.github.regyl.gfi.controller.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.service.feed.CycloneDxService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.SocketTimeoutException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "spring.properties.feature-enabled.feed-generation", havingValue = "true")
public class CycloneDxServiceImpl implements CycloneDxService {

    private static final String SBOM_PATH = "/sbom?url=";
    private static final HttpGet HEALTH_GET = new HttpGet("/health");

    private final Collection<HttpHost> cycloneDxHosts;
    @Qualifier("cdxgenHealthClient")
    private final CloseableHttpClient cdxgenHealthClient;
    @Qualifier("cdxgenSbomClient")
    private final CloseableHttpClient cdxgenSbomClient;
    private final AbstractHttpClientResponseHandler<HealthResponseDto> healthResponseHandler;
    private final AbstractHttpClientResponseHandler<SbomResponseDto> sbomResponseHandler;

    @Override
    public int getFreeServiceQuantity() {
        Collection<HttpHost> aliveHosts = getFreeHosts();
        return aliveHosts.size();
    }

    @Override
    public boolean isFree() {
        return getFreeServiceQuantity() == cycloneDxHosts.size();
    }

    @Async("cdxgenTaskPool")
    @Override
    public CompletableFuture<SbomResponseDto> getSbom(String url, HttpHost host) {
        HttpGet sbomGet = new HttpGet(SBOM_PATH + url);
        log.info("Using host {} and repository {}", host, url);
        try {
            SbomResponseDto response = cdxgenSbomClient.execute(host, sbomGet, sbomResponseHandler);
            return CompletableFuture.completedFuture(response);
        } catch (SocketTimeoutException e) {
            log.warn("Unable to process repository {} since it's huge", url);
            return CompletableFuture.failedFuture(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Queue<HttpHost> getFreeHosts() {
        List<HttpHost> freeHosts = cycloneDxHosts.stream().map(host -> {
            try {
                HealthResponseDto result = cdxgenHealthClient.execute(
                        host,
                        HEALTH_GET,
                        healthResponseHandler
                );
                if ("OK".equals(result.getStatus())) {
                    return host;
                } else {
                    log.error("CycloneDX status is not OK: {}", result.getStatus());
                    return null;
                }
            } catch (SocketTimeoutException e) {
                //info because timeout exceptions will be a common case (cdxgen is a single-thread service)
                log.info("Host {} is busy", host);
                return null;
            } catch (Exception e) {
                log.warn("Error while getting CycloneDX service status: {}", e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).toList();
        return new ArrayDeque<>(freeHosts);
    }
}

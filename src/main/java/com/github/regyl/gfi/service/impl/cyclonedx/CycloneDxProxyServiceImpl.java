package com.github.regyl.gfi.service.impl.cyclonedx;

import com.github.regyl.gfi.configuration.httpclient.HealthHttpClientResponseHandlerImpl;
import com.github.regyl.gfi.configuration.httpclient.SbomHttpClientResponseHandlerImpl;
import com.github.regyl.gfi.controller.dto.cyclonedx.HealthResponseDto;
import com.github.regyl.gfi.controller.dto.cyclonedx.SbomResponseDto;
import com.github.regyl.gfi.service.cyclonedx.CycloneDxProxyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class CycloneDxProxyServiceImpl implements CycloneDxProxyService {

    private static final String SBOM_PATH = "/sbom?url=";
    private static final HttpGet HEALTH_GET = new HttpGet("/health");

    private final Collection<HttpHost> cycloneDxHosts;
    @Qualifier("cdxgenHealthClient")
    private final CloseableHttpClient cdxgenHealthClient;
    @Qualifier("cdxgenSbomClient")
    private final CloseableHttpClient cdxgenSbomClient;

    @Override
    public int getFreeServiceQuantity() {
        Collection<HttpHost> aliveHosts = getFreeHosts();
        return aliveHosts.size();
    }

    @Override
    public boolean anyAlive() {
        return getFreeServiceQuantity() > 0;
    }

    @Async("cdxgenTaskPool")
    @Override
    public CompletableFuture<SbomResponseDto> getSbom(String url) {
        Collection<HttpHost> freeHosts = getFreeHosts();
        if (freeHosts.isEmpty()) {
            throw new IllegalStateException("No free cdxgen hosts available");
        }

        HttpHost host = freeHosts.iterator().next();
        HttpGet sbomGet = new HttpGet(SBOM_PATH + url);
        log.info("Using host: {}", host);
        try {
            SbomResponseDto response = cdxgenSbomClient.execute(host, sbomGet, new SbomHttpClientResponseHandlerImpl());
            return CompletableFuture.completedFuture(response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Collection<HttpHost> getFreeHosts() {
        return cycloneDxHosts.stream().map(host -> {
            try {
                HealthResponseDto result = cdxgenHealthClient.execute(host, HEALTH_GET, new HealthHttpClientResponseHandlerImpl());
                if ("OK".equals(result.getStatus())) {
                    return host;
                } else {
                    log.error("CycloneDX status is not OK: {}", result.getStatus());
                    return null;
                }
            } catch (Exception e) {
                //info because timeout exceptions will be a common case (cdxgen is a single-thread service)
                log.info("Error while getting CycloneDX service statuses: {}", e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).toList();
    }
}

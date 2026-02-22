package com.github.regyl.gfi.service.feed;

import com.github.regyl.gfi.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.service.StatefulService;
import org.apache.hc.core5.http.HttpHost;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

/**
 * Service for interacting with CycloneDX (cdxgen) services to generate SBOMs.
 * Manages a pool of CycloneDX service instances and provides async SBOM generation.
 * CycloneDX services are single-threaded, so health checks are used to determine availability.
 */
public interface CycloneDxService extends StatefulService {

    /**
     * Returns the number of available CycloneDX service instances.
     * A service is considered free if it responds to health check with "OK" status.
     *
     * @return number of free services
     */
    int getFreeServiceQuantity();

    /**
     * Generates SBOM for the given repository URL using the specified CycloneDX host.
     * Execution is async to avoid blocking the scheduler thread during long-running operations.
     * Large repositories may timeout; this is expected and handled gracefully.
     *
     * @param url GitHub repository URL
     * @param host CycloneDX service host to use
     * @return CompletableFuture that completes with SBOM response or fails with exception
     */
    CompletableFuture<SbomResponseDto> getSbom(String url, HttpHost host);

    /**
     * Returns a queue of available CycloneDX service hosts.
     * Only hosts that respond to health check with "OK" status are included.
     * Timeout exceptions during health check indicate the service is busy (single-threaded).
     *
     * @return queue of free hosts, empty if all services are busy
     */
    Queue<HttpHost> getFreeHosts();
}

package com.github.regyl.gfi.service.feed;

import com.github.regyl.gfi.controller.dto.cyclonedx.sbom.SbomResponseDto;
import org.apache.hc.core5.http.HttpHost;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

public interface CycloneDxService {

    boolean anyAlive();

    int getFreeServiceQuantity();

    CompletableFuture<SbomResponseDto> getSbom(String url, HttpHost host);

    Queue<HttpHost> getFreeHosts();
}

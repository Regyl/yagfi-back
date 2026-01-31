package com.github.regyl.gfi.service.cyclonedx;

import com.github.regyl.gfi.controller.dto.cyclonedx.SbomResponseDto;

import java.util.concurrent.CompletableFuture;

public interface CycloneDxProxyService {

    boolean anyAlive();

    int getFreeServiceQuantity();

    CompletableFuture<SbomResponseDto> getSbom(String url);
}

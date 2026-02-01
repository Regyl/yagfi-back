package com.github.regyl.gfi.service.impl.cyclonedx;

import com.github.regyl.gfi.controller.dto.cyclonedx.sbom.SbomResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CycloneDxResultConsumerServiceImpl implements BiConsumer<SbomResponseDto, Throwable> {

    @Override
    public void accept(SbomResponseDto dto, Throwable throwable) {
        if (throwable != null) {
            accept(throwable);
        } else {
            accept(dto);
        }
    }

    private void accept(Throwable throwable) {
        log.error(throwable.getMessage(), throwable);
    }

    private void accept(SbomResponseDto dto) {
        log.info(dto.toString());
    }
}

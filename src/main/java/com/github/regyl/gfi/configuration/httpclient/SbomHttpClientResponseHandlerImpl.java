package com.github.regyl.gfi.configuration.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.regyl.gfi.dto.cyclonedx.sbom.SbomResponseDto;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

@Contract(threading = ThreadingBehavior.STATELESS)
public class SbomHttpClientResponseHandlerImpl extends AbstractHttpClientResponseHandler<SbomResponseDto> {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Override
    public SbomResponseDto handleEntity(final HttpEntity entity) throws IOException {
        SbomResponseDto responseDto = MAPPER.readValue(entity.getContent(), SbomResponseDto.class);
        EntityUtils.consume(entity);
        return responseDto;
    }

    @Override
    public SbomResponseDto handleResponse(final ClassicHttpResponse response) throws IOException {
        return super.handleResponse(response);
    }
}

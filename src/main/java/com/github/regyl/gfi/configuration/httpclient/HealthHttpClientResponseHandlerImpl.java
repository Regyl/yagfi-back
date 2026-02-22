package com.github.regyl.gfi.configuration.httpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.regyl.gfi.dto.cyclonedx.health.HealthResponseDto;
import org.apache.hc.client5.http.impl.classic.AbstractHttpClientResponseHandler;
import org.apache.hc.core5.annotation.Contract;
import org.apache.hc.core5.annotation.ThreadingBehavior;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;

@Contract(threading = ThreadingBehavior.STATELESS)
public class HealthHttpClientResponseHandlerImpl extends AbstractHttpClientResponseHandler<HealthResponseDto> {

    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();

    @Override
    public HealthResponseDto handleEntity(final HttpEntity entity) throws IOException {
        HealthResponseDto responseDto = MAPPER.readValue(entity.getContent(), HealthResponseDto.class);
        EntityUtils.consume(entity);
        return responseDto;
    }

    @Override
    public HealthResponseDto handleResponse(final ClassicHttpResponse response) throws IOException {
        return super.handleResponse(response);
    }
}

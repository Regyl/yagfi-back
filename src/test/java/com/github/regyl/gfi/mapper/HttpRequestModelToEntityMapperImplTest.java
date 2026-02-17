package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.entity.LogEntity;
import com.github.regyl.gfi.model.HttpRequestModel;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HttpRequestModelToEntityMapperImplTest {

    private HttpRequestModelToEntityMapperImpl mapper;

    @BeforeEach
    void setUp() {
        mapper = new HttpRequestModelToEntityMapperImpl();
    }

    @Test
    void shouldReturnNullWhenInputIsNull() {
        LogEntity result = mapper.apply(null);
        assertThat(result).isNull();
    }

    @Test
    void shouldMapFullyFilledModel() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader(HttpHeaders.USER_AGENT)).thenReturn("Mozilla/5.0");
        when(request.getHeader("X-UTM-Source")).thenReturn("google");
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURL()).thenReturn(new StringBuffer("https://example.com/api"));
        when(request.getQueryString()).thenReturn("id=1");

        HttpRequestModel model =
                new HttpRequestModel(request, "test-body");

        LogEntity entity = mapper.apply(model);

        assertThat(entity).isNotNull();
        assertThat(entity.getHttpMethod()).isEqualTo("POST");
        assertThat(entity.getUrl()).isEqualTo("https://example.com/api?id=1");
        assertThat(entity.getRequestBody()).isEqualTo("test-body");
        assertThat(entity.getUtmSource()).isEqualTo("google");
    }
}

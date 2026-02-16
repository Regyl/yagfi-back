package com.github.regyl.gfi.service.impl.ipinfo;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.configuration.ipinfo.IpInfoConfigurationProperties;
import com.github.regyl.gfi.controller.dto.external.ipinfo.IpInfoResponseDto;
import com.github.regyl.gfi.feign.IpInfoClient;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DefaultUnitTest
class IpInfoServiceImplTest {

    private static final String TEST_IP = "1.2.3.4";
    private static final String TEST_TOKEN = "test-token";
    private static final String BEARER_TOKEN = "Bearer " + TEST_TOKEN;

    @Mock
    private IpInfoClient ipInfoClient;

    @Mock
    private IpInfoConfigurationProperties ipInfoConfig;

    @InjectMocks
    private IpInfoServiceImpl ipInfoService;

    @Test
    void getCountry_clientReturnsValidResponse_returnsCountryString() {
        IpInfoResponseDto response = new IpInfoResponseDto();
        response.setCountry("United States");
        when(ipInfoConfig.getToken()).thenReturn(TEST_TOKEN);
        when(ipInfoClient.getIpInfo(BEARER_TOKEN, TEST_IP)).thenReturn(response);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isEqualTo("United States");
    }

    @Test
    void getCountry_clientReturnsNull_returnsNull() {
        when(ipInfoConfig.getToken()).thenReturn(TEST_TOKEN);
        when(ipInfoClient.getIpInfo(BEARER_TOKEN, TEST_IP)).thenReturn(null);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isNull();
    }

    @Test
    void getCountry_clientThrowsException_returnsNull() {
        when(ipInfoConfig.getToken()).thenReturn(TEST_TOKEN);
        when(ipInfoClient.getIpInfo(BEARER_TOKEN, TEST_IP)).thenThrow(FeignException.Unauthorized.class);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isNull();
    }

    @Test
    void getCountry_responseHasNullCountry_returnsNull() {
        IpInfoResponseDto response = new IpInfoResponseDto();
        when(ipInfoConfig.getToken()).thenReturn(TEST_TOKEN);
        when(ipInfoClient.getIpInfo(BEARER_TOKEN, TEST_IP)).thenReturn(response);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isNull();
    }
}

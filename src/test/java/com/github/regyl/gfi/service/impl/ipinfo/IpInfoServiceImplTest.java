package com.github.regyl.gfi.service.impl.ipinfo;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
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

    @Mock
    private IpInfoClient ipInfoClient;

    @InjectMocks
    private IpInfoServiceImpl ipInfoService;

    @Test
    void getCountry_clientReturnsValidResponse_returnsCountryString() {
        IpInfoResponseDto response = new IpInfoResponseDto();
        response.setCountry("United States");
        when(ipInfoClient.getIpInfo(TEST_IP)).thenReturn(response);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isEqualTo("United States");
    }

    @Test
    void getCountry_clientReturnsNull_returnsNull() {
        when(ipInfoClient.getIpInfo(TEST_IP)).thenReturn(null);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isNull();
    }

    @Test
    void getCountry_clientThrowsException_returnsNull() {
        when(ipInfoClient.getIpInfo(TEST_IP)).thenThrow(FeignException.Unauthorized.class);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isNull();
    }

    @Test
    void getCountry_responseHasNullCountry_returnsNull() {
        IpInfoResponseDto response = new IpInfoResponseDto();
        when(ipInfoClient.getIpInfo(TEST_IP)).thenReturn(response);

        String result = ipInfoService.getCountry(TEST_IP);

        assertThat(result).isNull();
    }
}

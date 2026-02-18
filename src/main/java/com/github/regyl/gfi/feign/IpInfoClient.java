package com.github.regyl.gfi.feign;

import com.github.regyl.gfi.configuration.ipinfo.IpInfoFeignConfiguration;
import com.github.regyl.gfi.controller.dto.external.ipinfo.IpInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ipInfoClient", url = "https://ipinfo.io",
        configuration = IpInfoFeignConfiguration.class)
public interface IpInfoClient {

    @GetMapping("/lite/{ip}")
    IpInfoResponseDto getIpInfo(@PathVariable("ip") String ip);
}

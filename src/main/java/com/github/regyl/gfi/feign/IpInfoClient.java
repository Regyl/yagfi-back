package com.github.regyl.gfi.feign;

import com.github.regyl.gfi.controller.dto.external.ipinfo.IpInfoResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ipInfoClient", url = "https://ipinfo.io")
public interface IpInfoClient {

    @GetMapping("/lite/{ip}")
    IpInfoResponseDto getIpInfo(@RequestHeader("Authorization") String token,
                                @PathVariable("ip") String ip);
}

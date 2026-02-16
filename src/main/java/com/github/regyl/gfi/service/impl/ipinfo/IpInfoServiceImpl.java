package com.github.regyl.gfi.service.impl.ipinfo;

import com.github.regyl.gfi.configuration.ipinfo.IpInfoConfigurationProperties;
import com.github.regyl.gfi.controller.dto.external.ipinfo.IpInfoResponseDto;
import com.github.regyl.gfi.feign.IpInfoClient;
import com.github.regyl.gfi.service.ipinfo.IpInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpInfoServiceImpl implements IpInfoService {

    private final IpInfoClient ipInfoClient;

    private final IpInfoConfigurationProperties ipInfoConfig;

    @Override
    @Cacheable(cacheNames = "ipinfo", cacheManager = "ipInfoCacheManager",
            unless = "#result == null")
    public String getCountry(String ip) {
        String authToken = "Bearer " + ipInfoConfig.getToken();

        try {
            return Optional.ofNullable(ipInfoClient.getIpInfo(authToken, ip))
                    .map(IpInfoResponseDto::getCountry)
                    .orElse(null);
        } catch (Exception e) {
            log.warn("Failed to resolve country for IP: {}", ip, e);
            return null;
        }
    }
}

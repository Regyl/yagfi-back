package com.github.regyl.gfi.service.impl.ipinfo;

import com.github.regyl.gfi.dto.external.ipinfo.IpInfoResponseDto;
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

    @Override
    @Cacheable(cacheNames = "ipinfo", cacheManager = "ipInfoCacheManager",
            unless = "#result == null")
    public String getCountry(String ip) {
        try {
            return Optional.ofNullable(ipInfoClient.getIpInfo(ip))
                    .map(IpInfoResponseDto::getCountry)
                    .orElse(null);
        } catch (Exception e) {
            log.warn("Failed to resolve country for IP: {}", ip, e);
            return null;
        }
    }
}

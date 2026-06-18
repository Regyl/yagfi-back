package com.github.regyl.gfi.service.impl.ipinfo;

import com.github.regyl.gfi.dto.external.ipinfo.IpInfoResponseDto;
import com.github.regyl.gfi.feign.IpInfoClient;
import com.github.regyl.gfi.service.ipinfo.IpInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class IpInfoServiceImpl implements IpInfoService {

    private static final Collection<String> LOCAL_IPS = Set.of("0:0:0:0:0:0:0:1", "127.0.0.1");

    private final IpInfoClient ipInfoClient;

    @Override
    @Cacheable(cacheNames = "ipinfo", cacheManager = "ipInfoCacheManager",
            unless = "#result == null")
    public String getCountry(String ip) {
        if (LOCAL_IPS.contains(ip)) {
            return null;
        }

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

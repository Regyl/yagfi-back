package com.github.regyl.gfi.configuration.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.regyl.gfi.configuration.ipinfo.IpInfoConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfiguration {

    /**
     * Default cache manager using ConcurrentMapCacheManager. Simple in-memory cache
     * implementation that stores cache entries in a concurrent hash map.
     * @return CacheManager instance using ConcurrentMapCacheManager.
     */
    @Bean
    @Primary
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }

    /**
     * Cache manager using Caffeine for IP info caching. Uses Caffeine library to create
     * a cache manager with specific configurations for IP info caching.
     * @param ipInfoConfig Configuration properties for IP info caching, including cache
     *                     size and expiration settings.
     * @return CacheManager instance using Caffeine.
     */
    @Bean
    public CacheManager ipInfoCacheManager(IpInfoConfigurationProperties ipInfoConfig) {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("ipinfo");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(ipInfoConfig.getCacheSize())
                .expireAfterWrite(Duration.ofHours(24)));
        return cacheManager;
    }
}

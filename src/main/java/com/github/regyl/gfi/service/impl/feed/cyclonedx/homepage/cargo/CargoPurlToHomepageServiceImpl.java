package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.cargo;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.dto.external.crates.CratesIoResponseDto;
import com.github.regyl.gfi.feign.CratesIoClient;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Resolves Cargo package PURLs to homepage URLs via crates.io API.
 * crates.io provides homepage directly in the API response.
 * 
 * <p>Example: pkg:cargo/brotli@6.0.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CargoPurlToHomepageServiceImpl implements PurlToHomepageService {

    private final CratesIoClient cratesIoClient;

    @Override
    public boolean test(PackageURL purl) {
        return purl != null && purl.getType().equals("cargo");
    }

    /**
     * Resolves Cargo crate to homepage URL via crates.io API.
     * Returns null if crate doesn't exist or has no homepage.
     * 
     * @param purl Cargo package PURL
     * @return homepage URL or null
     */
    @Override
    public String apply(PackageURL purl) {
        if (purl == null) {
            return null;
        }

        String crateName = purl.getName();
        if (crateName == null || crateName.isEmpty()) {
            log.warn("Cargo purl has empty crate name: {}", purl);
            return null;
        }

        try {
            CratesIoResponseDto response = cratesIoClient.getCrate(crateName);
            if (response == null || response.getCrate() == null) {
                log.warn("No crate information found for: {}", crateName);
                return null;
            }

            return response.getCrate().getHomepage();
        } catch (FeignException.NotFound e) {
            log.warn("Crate does not exists: {}", crateName);
            return null;
        } catch (Exception e) {
            log.error("Failed to fetch homepage for crate '{}' from crates.io", crateName, e);
            return null;
        }
    }
}

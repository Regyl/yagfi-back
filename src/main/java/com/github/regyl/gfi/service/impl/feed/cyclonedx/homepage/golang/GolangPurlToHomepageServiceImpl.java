package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.golang;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToBuildFileService;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToHomepageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service for fetching Go packages.
 * Example: pkg:golang/github.com/dghubble/oauth1@v0.7.3, pkg:golang/aead.dev/minisign@v0.2.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class GolangPurlToHomepageServiceImpl implements PurlToHomepageService {

    private final Collection<PurlToBuildFileService> buildFileServices;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("golang");
    }

    @Override
    public String apply(PackageURL purl) {
        log.warn("Not implemented yet for purl {}", purl);
        return null;
    }
}

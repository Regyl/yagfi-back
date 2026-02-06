package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.go;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import org.springframework.stereotype.Component;

/**
 * Service for fetching github packages.
 * Example: pkg:github/actions/checkout@v4
 */
@Component
public class GoPurlToHomepageServiceImpl implements PurlToHomepageService {

    @Override
    public boolean test(PackageURL purl) {
        return "golang".equals(purl.getType()) && purl.getNamespace() != null
                && purl.getNamespace().startsWith("github.com");
    }

    @Override
    public String apply(PackageURL purl) {
        return String.format("https://%s/%s", purl.getNamespace(), purl.getName());
    }
}

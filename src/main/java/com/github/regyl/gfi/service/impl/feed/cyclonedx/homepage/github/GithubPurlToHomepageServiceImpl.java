package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.github;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToHomepageService;
import org.springframework.stereotype.Component;

/**
 * Service for fetching github packages.
 * Example: pkg:github/actions/checkout@v4
 */
@Component
public class GithubPurlToHomepageServiceImpl implements PurlToHomepageService {

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("github");
    }

    @Override
    public String apply(PackageURL purl) {
        return String.format("https://github.com/%s/%s", purl.getNamespace(), purl.getName());
    }
}

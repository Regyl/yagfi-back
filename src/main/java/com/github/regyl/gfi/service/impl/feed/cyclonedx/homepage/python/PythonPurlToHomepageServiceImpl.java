package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.python;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToBuildFileService;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToHomepageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Service for fetching Go packages.
 * Example: pkg:pypi/faiss-cpu@1.12.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PythonPurlToHomepageServiceImpl implements PurlToHomepageService {

    private final Collection<PurlToBuildFileService> buildFileServices;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("pypi");
    }

    @Override
    public String apply(PackageURL purl) {
        log.warn("Not implemented yet for purl {}", purl);
        return null;
    }
}

package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.python;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.dto.external.pypi.PypiResponseDto;
import com.github.regyl.gfi.feign.PypiClient;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import com.github.regyl.gfi.util.LinkUtil;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Resolves Python package PURLs to GitHub repository URLs via PyPI API.
 * 
 * <p>Extracts homepage with priority: project_urls.Source (if GitHub) ->
 * project_urls.Homepage (if GitHub) -> home_page. We prefer GitHub URLs
 * from project_urls over generic home_page to match dependencies to repositories.
 * 
 * <p>Example: pkg:pypi/pytz@2025.2
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class PythonPurlToHomepageServiceImpl implements PurlToHomepageService {

    private final PypiClient pypiClient;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("pypi");
    }

    /**
     * Resolves Python package to GitHub repository URL via PyPI API.
     * Returns null if package doesn't exist, has no homepage, or homepage is not a GitHub URL.
     * 
     * @param purl Python package PURL
     * @return normalized GitHub repository URL or null
     */
    @Override
    public String apply(PackageURL purl) {
        if (purl == null) {
            return null;
        }

        String packageName = purl.getName();
        if (packageName == null || packageName.isEmpty()) {
            log.warn("Python purl has empty package name: {}", purl);
            return null;
        }

        try {
            PypiResponseDto response = pypiClient.getPackage(packageName);
            if (response == null || response.getInfo() == null) {
                log.warn("No package information found for: {}", packageName);
                return null;
            }

            PypiResponseDto.InfoDto info = response.getInfo();
            String homepage = extractHomepage(info);
            return LinkUtil.normalizeRepositoryUrl(homepage);
        } catch (FeignException.NotFound e) {
            log.warn("Package does not exist: {}", packageName);
            return null;
        } catch (Exception e) {
            log.error("Failed to fetch homepage for package '{}' from pypi.org", packageName, e);
            return null;
        }
    }

    private String extractHomepage(PypiResponseDto.InfoDto info) {
        if (info.getProjectUrls() != null && info.getProjectUrls().getSource() != null) {
            String source = info.getProjectUrls().getSource();
            if (isGitHubUrl(source)) {
                return source;
            }
        }

        if (info.getProjectUrls() != null && isGitHubUrl(info.getProjectUrls().getHomepage())) {
            return info.getProjectUrls().getHomepage();
        }

        return info.getHomePage();
    }

    private boolean isGitHubUrl(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }
        return url.contains("github.com");
    }
}

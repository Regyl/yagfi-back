package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.npm;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.PurlToBuildFileService;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import com.github.regyl.gfi.util.LinkUtil;
import com.github.regyl.gfi.util.ServicePredicateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class NpmPurlToHomepageServiceImpl implements PurlToHomepageService {

    private final ObjectMapper mapper;
    private final Collection<PurlToBuildFileService> buildFileServices;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("npm");
    }

    @Override
    public String apply(PackageURL purl) {
        if (purl == null) {
            return null;
        }

        PurlToBuildFileService buildFileService = ServicePredicateUtil.getTargetService(buildFileServices, purl);
        String buildFile = buildFileService.apply(purl);
        if (buildFile == null) {
            return null;
        }

        String repoUrl = extractGithubUrlFromPackageJson(buildFile);
        return LinkUtil.normalizeRepositoryUrl(repoUrl);
    }

    private String extractGithubUrlFromPackageJson(String packageJson) {
        try {
            JsonNode root = mapper.readTree(packageJson);

            if (root.has("repository") && root.get("repository").has("url")) {
                return root.get("repository").get("url").asText();
            }

            if (root.has("repository") && root.get("repository").isTextual() && root.get("repository").asText().contains("git")) {
                return root.get("repository").asText();
            }

            if (root.has("homepage")) {
                return root.get("homepage").asText();
            }

            return null;
        } catch (Exception e) {
            log.warn("Failed to parse package.json", e);
            return null;
        }
    }
}

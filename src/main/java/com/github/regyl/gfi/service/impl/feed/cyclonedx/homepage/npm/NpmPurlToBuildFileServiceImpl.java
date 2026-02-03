package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.npm;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToBuildFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
public class NpmPurlToBuildFileServiceImpl implements PurlToBuildFileService {

    private static final String NPM_REGISTRY_BASE_URL = "https://registry.npmjs.org";

    private final RestClient restClient;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("npm");
    }

    @Override
    public String apply(PackageURL purl) {
        String groupId = purl.getNamespace();
        String artifactId = purl.getName();
        String version = purl.getVersion();

        String pomUrl = buildPomUrl(groupId, artifactId, version);
        return downloadBuildFile(pomUrl);
    }

    private String buildPomUrl(String groupId, String artifactId, String version) {
        return Stream.of(NPM_REGISTRY_BASE_URL, groupId, artifactId, version)
                .filter(Objects::nonNull)
                .collect(Collectors.joining("/"));
    }

    private String downloadBuildFile(String registryUrl) {
        try {
            return restClient.get()
                    .uri(registryUrl)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            log.warn("Failed to fetch package.json from npm registry: {}", registryUrl, e);
            return null;
        }
    }
}

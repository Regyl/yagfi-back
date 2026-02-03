package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.maven;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToBuildFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class MavenPurlToBuildFileServiceImpl implements PurlToBuildFileService {

    private static final String MAVEN_CENTRAL_BASE_URL = "https://repo1.maven.org/maven2";

    private final RestClient restClient;

    @Override
    public boolean test(PackageURL purl) {
        return purl.getType().equals("maven");
    }

    @Override
    public String apply(PackageURL purl) {
        String groupId = purl.getNamespace();
        String artifactId = purl.getName();
        String version = purl.getVersion();

        String pomUrl = buildPomUrl(groupId, artifactId, version);
        return downloadBuildFile(pomUrl);
    }

    private String buildPomUrl(String group, String artifact, String version) {
        String groupPath = group.replace('.', '/');
        return String.format("%s/%s/%s/%s/%s-%s.pom",
                MAVEN_CENTRAL_BASE_URL, groupPath, artifact, version, artifact, version);
    }

    private String downloadBuildFile(String pomUrl) {
        try {
            return restClient.get()
                    .uri(pomUrl)
                    .retrieve()
                    .body(String.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Not found pom.xml by url {}", pomUrl);
            return null;
        } catch (Exception e) {
            log.warn("Failed to fetch POM from Maven Central: {}", pomUrl, e);
            return null;
        }
    }
}

package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.github;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.annotation.DefaultUnitTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DefaultUnitTest
class GithubPurlToHomepageServiceImplTest {

    private GithubPurlToHomepageServiceImpl service = new GithubPurlToHomepageServiceImpl();

    @Test
    void shouldConvertPurlToGithubHomepage() throws Exception {
        //Given
        PackageURL purl = new PackageURL("pkg:github/actions/checkout@v4");

        //When
        String result = service.apply(purl);

        //Then
        assertThat(result).isEqualTo("https://github.com/actions/checkout");
    }
}

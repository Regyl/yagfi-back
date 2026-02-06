package com.github.regyl.gfi.service.impl.feed.cyclonedx.homepage.go;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.annotation.DefaultUnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

@DefaultUnitTest
class GoPurlToHomepageServiceImplTest {

    @InjectMocks
    private GoPurlToHomepageServiceImpl target;

    @Test
    void testApply() throws MalformedPackageURLException {
        String purlString = "pkg:golang/github.com/docker/go-units@v0.5.0";
        PackageURL purl = new PackageURL(purlString);

        String result = target.apply(purl);

        Assertions.assertThat(result).isEqualTo("https://github.com/docker/go-units");
    }

    @Test
    void testTest() throws MalformedPackageURLException {
        String purlString = "pkg:golang/github.com/docker/go-units@v0.5.0";
        PackageURL purl = new PackageURL(purlString);

        boolean result = target.test(purl);

        Assertions.assertThat(result).isEqualTo(true);
    }
}

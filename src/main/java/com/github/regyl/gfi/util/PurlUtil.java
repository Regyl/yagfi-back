package com.github.regyl.gfi.util;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public class PurlUtil {

    public static PackageURL toPurl(String purl) {
        try {
            return new PackageURL(purl);
        } catch (MalformedPackageURLException e) {
            log.warn("Error parsing purl {} with message {}", purl, e.getMessage());
            return null;
        }
    }
}

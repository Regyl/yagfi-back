package com.github.regyl.gfi.service.feed.cyclonedx;

import com.github.packageurl.PackageURL;

import java.util.function.Function;
import java.util.function.Predicate;

public interface PurlToHomepageService extends Predicate<PackageURL>, Function<PackageURL, String> {
}

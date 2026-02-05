package com.github.regyl.gfi.service.feed;

import com.github.packageurl.PackageURL;

import java.util.function.Function;
import java.util.function.Predicate;

public interface PurlToBuildFileService extends Predicate<PackageURL>, Function<PackageURL, String> {
}

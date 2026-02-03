package com.github.regyl.gfi.util;

import com.github.packageurl.MalformedPackageURLException;
import com.github.packageurl.PackageURL;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class PurlUtil {

    private static final Pattern MAVEN_PURL_PATTERN = Pattern.compile("pkg:maven/([^/@]+)/([^/@]+)");
    private static final Pattern GITHUB_COM_PATTERN = Pattern.compile("com\\.github\\.([^.]+)(?:\\.(.+))?");
    private static final Pattern GITHUB_IO_PATTERN = Pattern.compile("io\\.github\\.([^.]+)(?:\\.(.+))?");

    public static PackageURL toPurl(String purl) {
        try {
            return new PackageURL(purl);
        } catch (MalformedPackageURLException e) {
            log.warn("Error parsing purl {} with message {}", purl, e.getMessage());
            return null;
        }
    }

    public static String extractGithubUrl(String purl) {
        if (purl == null || purl.isEmpty()) {
            return null;
        }

        Matcher mavenMatcher = MAVEN_PURL_PATTERN.matcher(purl);
        if (!mavenMatcher.find()) {
            return null;
        }

        String group = mavenMatcher.group(1);
        String name = mavenMatcher.group(2);

        // Проверяем паттерн com.github.*
        Matcher githubMatcher = GITHUB_COM_PATTERN.matcher(group);
        if (githubMatcher.matches()) {
            String username = githubMatcher.group(1);
            String subGroup = githubMatcher.group(2);
            if (subGroup != null && !subGroup.isEmpty()) {
                // Если есть подгруппа, используем её как имя репозитория
                return String.format("https://github.com/%s/%s", username, subGroup);
            } else {
                // Иначе используем name из purl как имя репозитория
                return String.format("https://github.com/%s/%s", username, name);
            }
        }

        // Проверяем паттерн io.github.*
        Matcher githubIoMatcher = GITHUB_IO_PATTERN.matcher(group);
        if (githubIoMatcher.matches()) {
            String username = githubIoMatcher.group(1);
            String subGroup = githubIoMatcher.group(2);
            if (subGroup != null && !subGroup.isEmpty()) {
                return String.format("https://github.com/%s/%s", username, subGroup);
            } else {
                return String.format("https://github.com/%s/%s", username, name);
            }
        }

        return null;
    }
}

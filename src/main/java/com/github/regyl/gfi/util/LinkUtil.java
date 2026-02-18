package com.github.regyl.gfi.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class LinkUtil {

    public static String normalizeRepositoryUrl(String url) {
        if (url == null || url.isEmpty()) {
            return null;
        }

        if (url.startsWith("git+ssh://git@github.com")) {
            url = url.replace("git+ssh://git@github.com", "https://github.com");
        }

        if (url.startsWith("git+")) {
            url = url.replace("git+", "");
        }

        if (url.startsWith("git@github.com:")) {
            url = url.replace("git@github.com:", "https://github.com/");
        }

        if (url.startsWith("scm:git@github.com:")) {
            url = url.replace("scm:git@github.com:", "https://github.com/");
        }

        if (url.startsWith("scm:git:")) {
            url = url.replace("scm:git:", "");
        }

        if (url.startsWith("git://")) {
            url = url.replace("git://", "https://");
        }

        if (url.startsWith("http://")) {
            url = url.replace("http://", "https://");
        }

        int dotGitIndex = url.indexOf(".git");
        if (dotGitIndex != -1) {
            url = url.substring(0, dotGitIndex);
        }

        int treeIndex = url.indexOf("/tree/");
        if (treeIndex != -1) {
            url = url.substring(0, treeIndex);
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }
}

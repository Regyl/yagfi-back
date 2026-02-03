package com.github.regyl.gfi.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ua_parser.Client;
import ua_parser.Parser;

@Slf4j
@UtilityClass
public class UserAgentParser {

    private static final Parser PARSER = new Parser();

    public static String parseOS(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        try {
            Client client = PARSER.parse(userAgent);
            String osFamily = client.os != null ? client.os.family : null;
            if (osFamily != null) {
                String osLower = osFamily.toLowerCase();
                if (osLower.contains("android")) {
                    return "Android";
                } else if (osLower.contains("ios") || osLower.contains("iphone") || osLower.contains("ipad")) {
                    return "iOS";
                } else if (osLower.contains("windows")) {
                    return "Windows";
                } else if (osLower.contains("mac")) {
                    return "macOS";
                } else if (osLower.contains("linux")) {
                    return "Linux";
                }
                return osFamily;
            }
        } catch (Exception e) {
            log.error("Error while parsing user agent: {}", userAgent, e);
        }

        return null;
    }

    public static String parseBrowserFamily(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }

        try {
            Client client = PARSER.parse(userAgent);
            return client.userAgent != null ? client.userAgent.family : null;
        } catch (Exception e) {
            log.error("Error while parsing user agent: {}", userAgent, e);
        }

        return null;
    }

    public static String parseDeviceType(String userAgent) {
        if (userAgent == null || userAgent.isEmpty()) {
            return null;
        }
        try {
            Client client = PARSER.parse(userAgent);
            String deviceFamily = client.device != null ? client.device.family : null;
            if (deviceFamily != null) {
                String deviceLower = deviceFamily.toLowerCase();
                if (deviceLower.contains("mobile") || deviceLower.contains("phone")) {
                    return "mobile";
                } else if (deviceLower.contains("tablet") || deviceLower.contains("ipad")) {
                    return "tablet";
                } else {
                    return "desktop";
                }
            }

            String os = parseOS(userAgent);
            if (os != null) {
                String osLower = os.toLowerCase();
                if (osLower.contains("android") || osLower.contains("ios")) {
                    if (userAgent.toLowerCase().contains("mobile") && !userAgent.toLowerCase().contains("tablet")) {
                        return "mobile";
                    } else if (userAgent.toLowerCase().contains("tablet") || userAgent.toLowerCase().contains("ipad")) {
                        return "tablet";
                    }
                    return "mobile";
                }
            }
        } catch (Exception e) {
            log.error("Error while parsing user agent: {}", userAgent, e);
        }

        return "desktop";
    }
}

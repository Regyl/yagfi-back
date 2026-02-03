package com.github.regyl.gfi.service.impl;

import com.github.regyl.gfi.entity.LogEntity;
import com.github.regyl.gfi.repository.LogRepository;
import com.github.regyl.gfi.service.LogService;
import com.github.regyl.gfi.util.UserAgentParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Async
    @Override
    public void logRequest(HttpServletRequest request, String requestBody) {
        try {
            String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            String country = getCountryFromRequest(request);
            String url = getFullRequestUrl(request);

            LogEntity logEntity = LogEntity.builder()
                    .url(url)
                    .httpMethod(request.getMethod())
                    .requestBody(requestBody)
                    .country(country)
                    .os(UserAgentParser.parseOS(userAgent))
                    .browserFamily(UserAgentParser.parseBrowserFamily(userAgent))
                    .deviceType(UserAgentParser.parseDeviceType(userAgent))
                    .build();

            logRepository.save(logEntity);
        } catch (IllegalStateException e) {
            log.warn("Illegal state of request during saving: {}", e.getMessage());
        } catch (Exception e) {
            log.error("Failed to log HTTP request", e);
        }
    }

    private String getCountryFromRequest(HttpServletRequest request) {
        return null; //TODO no way to get country properly
    }

    private String getFullRequestUrl(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }

        return requestURL.toString();
    }
}

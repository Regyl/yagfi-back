package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.entity.LogEntity;
import com.github.regyl.gfi.model.HttpRequestModel;
import com.github.regyl.gfi.util.UserAgentParser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class HttpRequestModelToEntityMapperServiceImpl implements Function<HttpRequestModel, LogEntity> {

    @Override
    public LogEntity apply(HttpRequestModel model) {
        HttpServletRequest request = model.getRequest();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String country = getCountryFromRequest(request);
        String url = getFullRequestUrl(request);

        return LogEntity.builder()
                .url(url)
                .httpMethod(request.getMethod())
                .requestBody(model.getRequestBody())
                .country(country)
                .os(UserAgentParser.parseOS(userAgent))
                .browserFamily(UserAgentParser.parseBrowserFamily(userAgent))
                .deviceType(UserAgentParser.parseDeviceType(userAgent))
                .build();
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

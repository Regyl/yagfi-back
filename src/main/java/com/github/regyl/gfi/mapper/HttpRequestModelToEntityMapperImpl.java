package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.entity.LogEntity;
import com.github.regyl.gfi.model.HttpRequestModel;
import com.github.regyl.gfi.service.ipinfo.IpInfoService;
import com.github.regyl.gfi.util.UserAgentUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class HttpRequestModelToEntityMapperImpl implements Function<HttpRequestModel, LogEntity> {

    private final IpInfoService ipInfoService;

    @Override
    public LogEntity apply(HttpRequestModel model) {
        if (model == null) {
            return null;
        }

        HttpServletRequest request = model.getRequest();
        String userAgent = request.getHeader(HttpHeaders.USER_AGENT);
        String country = getCountryFromRequest(request);
        String url = getFullRequestUrl(request);
        String utmSource = request.getHeader("X-UTM-Source");

        return LogEntity.builder()
                .url(url)
                .httpMethod(request.getMethod())
                .requestBody(model.getRequestBody())
                .country(country)
                .os(UserAgentUtil.parseOS(userAgent))
                .browserFamily(UserAgentUtil.parseBrowserFamily(userAgent))
                .deviceType(UserAgentUtil.parseDeviceType(userAgent))
                .utmSource(utmSource)
                .build();
    }

    private String getCountryFromRequest(HttpServletRequest request) {
        return ipInfoService.getCountry(request.getRemoteAddr());
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

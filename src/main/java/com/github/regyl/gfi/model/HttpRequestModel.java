package com.github.regyl.gfi.model;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class HttpRequestModel {

    private HttpServletRequest request;

    private String requestBody;
}

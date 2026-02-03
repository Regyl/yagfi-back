package com.github.regyl.gfi.service;

import jakarta.servlet.http.HttpServletRequest;

public interface LogService {

    void logRequest(HttpServletRequest request, String requestBody);
}

package com.github.regyl.gfi.service.other;

import jakarta.servlet.http.HttpServletRequest;

public interface LogService {

    void logRequest(HttpServletRequest request, String requestBody);
}

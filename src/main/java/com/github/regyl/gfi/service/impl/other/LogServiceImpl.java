package com.github.regyl.gfi.service.impl.other;

import com.github.regyl.gfi.entity.LogEntity;
import com.github.regyl.gfi.model.HttpRequestModel;
import com.github.regyl.gfi.repository.LogRepository;
import com.github.regyl.gfi.service.other.LogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;
    private final Function<HttpRequestModel, LogEntity> mapper;

    @Async
    @Override
    public void logRequest(HttpServletRequest request, String requestBody) {
        try {
            HttpRequestModel model = new HttpRequestModel(request, requestBody);
            LogEntity logEntity = mapper.apply(model);
            logRepository.save(logEntity);
        } catch (Exception e) {
            log.error("Failed to log HTTP request", e);
        }
    }
}

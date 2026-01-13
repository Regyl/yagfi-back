package com.github.regyl.gfi.configuration.async;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class DefaultAsyncUncaughtExceptionHandlerImpl implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(@NonNull Throwable ex, Method method, @Nullable Object... params) {
        while (ex.getCause() != null) {
            ex = ex.getCause();
        }

        log.error("Async uncaught exception, ex: {}, method: {}, params: {}", ex.getMessage(), method.getName(), params);
    }
}

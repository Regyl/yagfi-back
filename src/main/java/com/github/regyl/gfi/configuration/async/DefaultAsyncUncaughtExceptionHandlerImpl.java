package com.github.regyl.gfi.configuration.async;

import jakarta.annotation.Nullable;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

@Slf4j
public class DefaultAsyncUncaughtExceptionHandlerImpl implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(@NonNull Throwable ex, @NonNull Method method, @Nullable Object... params) {
        while (ex.getCause() != null) {
            ex = ex.getCause();
        }

        if (log.isDebugEnabled()) {
            log.error("Async uncaught exception of type {} with message {}. method: {}, params: {}",
                    ex.getClass(), ex.getMessage(), method.getName(), params
            );
        } else {
            log.error("Async uncaught exception of type {} with message {}. method: {}",
                    ex.getClass(), ex.getMessage(), method.getName()
            );
        }
    }
}

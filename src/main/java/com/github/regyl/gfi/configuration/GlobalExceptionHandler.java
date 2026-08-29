package com.github.regyl.gfi.configuration;

import com.github.regyl.gfi.dto.response.ErrorResponseDto;
import com.github.regyl.gfi.exception.EventNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Supplier<OffsetDateTime> dateTimeSupplier;

    @ExceptionHandler(EventNotFoundException.class)
    public ErrorResponseDto handleEventNotFoundException(EventNotFoundException e) {
        log.error("ActionLog.eventNotFoundException ", e);
        return buildResponse(HttpStatus.NOT_FOUND, "Event Not Found Exception");
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ErrorResponseDto handleHttpClientErrorException(HttpClientErrorException.Unauthorized ex) {
        log.error("ActionLog.handleUnauthorized - Auth failure: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Git API authorization failed. Check your token.");
    }

    private ErrorResponseDto buildResponse(HttpStatus status, String message) {
        return ErrorResponseDto.builder()
                .timestamp(dateTimeSupplier.get())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
    }
}

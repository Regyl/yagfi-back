package com.github.regyl.gfi.exception;

import com.github.regyl.gfi.exception.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(EventNotFoundException.class)
    public ErrorResponse handleEventNotFoundException(EventNotFoundException e) {
        log.error("ActionLog.eventNotFoundException ", e);
        return buildResponse(HttpStatus.NOT_FOUND, "Event Not Found Exception");
    }

    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ErrorResponse handleHttpClientErrorException(HttpClientErrorException.Unauthorized ex) {
        log.error("ActionLog.handleUnauthorized - Auth failure: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, "Git API authorization failed. Check your token.");
    }

    private ErrorResponse buildResponse(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .build();
    }
}

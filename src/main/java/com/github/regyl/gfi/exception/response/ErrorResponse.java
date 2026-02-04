package com.github.regyl.gfi.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private int status;
    private String path;
    private String error;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> details;
}
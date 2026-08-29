package com.github.regyl.gfi.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {

    private int status;
    private String path;
    private String error;
    private String message;
    private OffsetDateTime timestamp;
    private Map<String, String> details;
}
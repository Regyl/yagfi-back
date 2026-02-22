package com.github.regyl.gfi.dto.external.crates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CratesIoResponseDto {

    @JsonProperty("crate")
    private CrateDto crate;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CrateDto {
        @JsonProperty("homepage")
        private String homepage;
    }
}

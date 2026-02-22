package com.github.regyl.gfi.dto.external.pypi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PypiResponseDto {

    @JsonProperty("info")
    private InfoDto info;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InfoDto {
        @JsonProperty("home_page")
        private String homePage;

        @JsonProperty("project_urls")
        private ProjectUrlsDto projectUrls;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProjectUrlsDto {
        @JsonProperty("Source")
        private String source;

        @JsonProperty("Homepage")
        private String homepage;
    }
}

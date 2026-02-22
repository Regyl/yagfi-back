package com.github.regyl.gfi.dto.github.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepositoryDto {

    private String id;
    private String nameWithOwner;
    private String url;
    private String description;
    private Integer stargazerCount;
    private OffsetDateTime updatedAt;
    private GithubLanguageDto primaryLanguage;
    private GithubLicenseInfo licenseInfo;

}

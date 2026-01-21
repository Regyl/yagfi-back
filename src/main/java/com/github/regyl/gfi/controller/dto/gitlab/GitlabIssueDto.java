package com.github.regyl.gfi.controller.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabIssueDto {

    private String id;
    private String title;
    private String webUrl;
    private String state;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
}

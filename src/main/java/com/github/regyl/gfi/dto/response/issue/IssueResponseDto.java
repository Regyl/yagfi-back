package com.github.regyl.gfi.dto.response.issue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class IssueResponseDto {

    private Long issueId;
    private String issueTitle;
    private String issueUrl;
    private OffsetDateTime issueUpdated;
    private OffsetDateTime issueCreated;
    private List<String> issueLabels;
    private String issueLanguage;

    private String repositoryTitle;
    private String repositoryUrl;
    private Integer repositoryStars;
    private String repositoryDescription;
    private String repositoryLanguage;
    private String repositoryLicense;
}

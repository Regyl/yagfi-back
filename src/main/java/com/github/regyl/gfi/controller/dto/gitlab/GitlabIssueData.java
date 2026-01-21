package com.github.regyl.gfi.controller.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabIssueData {

    private GitlabSearchDto issues;

    @JsonIgnore
    public String getEndCursor() {
        if (issues == null) {
            return null;
        }

        GitlabPageInfo pageInfo = issues.getPageInfo();
        if (pageInfo == null) {
            return null;
        }

        return pageInfo.getEndCursor();
    }
}

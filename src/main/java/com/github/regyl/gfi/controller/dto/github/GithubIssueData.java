package com.github.regyl.gfi.controller.dto.github;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubIssueData {

    private GithubRateLimit rateLimit;
    private GithubSearchDto search;

    @JsonIgnore
    public String getEndCursor() {
        if (search == null) {
            return null;
        }

        GithubPageInfo pageInfo = search.getPageInfo();
        if (pageInfo == null) {
            return null;
        }

        return pageInfo.getEndCursor();
    }
}

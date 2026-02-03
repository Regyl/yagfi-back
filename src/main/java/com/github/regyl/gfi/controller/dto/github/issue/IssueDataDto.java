package com.github.regyl.gfi.controller.dto.github.issue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueDataDto {

    private GithubRateLimitDto rateLimit;
    private GithubSearchDto search;

    @JsonIgnore
    public String getEndCursor() {
        if (search == null) {
            return null;
        }

        GithubPageInfoDto pageInfo = search.getPageInfo();
        if (pageInfo == null) {
            return null;
        }

        return pageInfo.getEndCursor();
    }
}

package com.github.regyl.gfi.dto.github.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubSearchDto {

    private GithubPageInfoDto pageInfo;
    private List<GithubIssueDto> nodes;
}

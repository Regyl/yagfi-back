package com.github.regyl.gfi.controller.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabSearchDto {

    private GitlabPageInfo pageInfo;
    private List<GitlabIssueDto> nodes;
}

package com.github.regyl.gfi.controller.dto.gitlab;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitlabPageInfo {

    private boolean hasNextPage;
    private String endCursor;
}

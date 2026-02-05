package com.github.regyl.gfi.controller.dto.github.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubIssueDto {

    private String id;
    private int number;
    private String title;
    private String url;
    private GithubIssueState state;
    private OffsetDateTime updatedAt;
    private OffsetDateTime createdAt;
    private GithubRepositoryDto repository;
    private GithubLabelsDto labels;

    public List<String> getConvertedLabels() {
        if (labels == null) {
            return Collections.emptyList();
        }

        List<GithubLabelDto> nodes = labels.getNodes();
        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptyList();
        }

        return nodes.stream()
                .map(GithubLabelDto::getName)
                .toList();
    }
}

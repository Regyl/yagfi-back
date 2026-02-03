package com.github.regyl.gfi.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class IssueEntity extends AbstractEntity {

    @NotNull
    private String sourceId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String url;

    @NotNull
    private OffsetDateTime updatedAt;

    @NotNull
    private OffsetDateTime createdAt;

    private RepositoryEntity repository;

    @NotNull
    private Long repositoryId;

    private List<String> labels;
}

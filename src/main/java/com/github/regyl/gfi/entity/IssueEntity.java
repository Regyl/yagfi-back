package com.github.regyl.gfi.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.OffsetDateTime;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "e_issue")
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class IssueEntity extends AbstractEntity {

    @NotNull
    @Column(unique = true)
    private String sourceId;

    @NotEmpty
    @Column(columnDefinition = "TEXT")
    private String title;

    @NotEmpty
    private String url;

    @NotNull
    private OffsetDateTime updatedAt;
    @NotNull
    private OffsetDateTime createdAt;

    @ManyToOne
    private RepositoryEntity repository;

    @NotNull
    @Column(name = "repository_id", insertable = false, updatable = false)
    private Long repositoryId;
}

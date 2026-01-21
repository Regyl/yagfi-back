package com.github.regyl.gfi.entity;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RepositoryEntity extends AbstractEntity {

    @NotNull
    private String sourceId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String url;

    @NotNull
    private Integer stars;

    private String description;

    private String language;

    private String source;
}

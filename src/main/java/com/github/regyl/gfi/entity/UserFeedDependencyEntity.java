package com.github.regyl.gfi.entity;

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
public class UserFeedDependencyEntity extends AbstractEntity {

    /**
     * ID of the entity below.
     *
     * @see UserFeedRequestEntity
     */
    private Long requestId;
    private String sourceRepo;
    private String dependencyUrl;
}

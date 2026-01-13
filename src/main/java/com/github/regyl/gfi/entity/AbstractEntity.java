package com.github.regyl.gfi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
@OnDelete(action = OnDeleteAction.CASCADE)
public abstract class AbstractEntity {

    @Id
    @Column(columnDefinition = "BIGSERIAL")
    private Long id;


    @Column(name = "created", columnDefinition = "TIMESTAMP NOT NULL DEFAULT NOW()", updatable = false)
    @CreationTimestamp
    private OffsetDateTime created;
}

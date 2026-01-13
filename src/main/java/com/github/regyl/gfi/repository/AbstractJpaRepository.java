package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AbstractJpaRepository<T extends AbstractEntity> extends JpaRepository<T, Long> {
}

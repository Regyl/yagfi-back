package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.RepositoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface RepoJpaRepository extends AbstractJpaRepository<RepositoryEntity> {
}

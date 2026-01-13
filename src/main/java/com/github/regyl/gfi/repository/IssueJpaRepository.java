package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.IssueEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueJpaRepository extends AbstractJpaRepository<IssueEntity> {
}

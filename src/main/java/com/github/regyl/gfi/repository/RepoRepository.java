package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.RepositoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Set;

@Mapper
public interface RepoRepository {

    void saveAll(@Param("entities") Set<RepositoryEntity> entities, @Param("tableName") String tableName);

    Collection<RepositoryEntity> findAll(@Param("tableName") String tableName, @Param("source") String source);
}

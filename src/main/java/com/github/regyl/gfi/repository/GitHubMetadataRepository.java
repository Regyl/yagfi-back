package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.GitHubMetadataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

@Mapper
public interface GitHubMetadataRepository {

    void save(@Param("entity") GitHubMetadataEntity entity);

    void saveAll(@Param("entities") Collection<GitHubMetadataEntity> entities);

    Collection<GitHubMetadataEntity> findAll();
}

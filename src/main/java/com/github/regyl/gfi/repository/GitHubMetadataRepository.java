package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.GitHubMetadataEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.Optional;

@Mapper
public interface GitHubMetadataRepository {

    void saveAll(@Param("entities") Collection<GitHubMetadataEntity> entities);

    Optional<GitHubMetadataEntity> findByLabel(@Param("label") String label);
}

package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.annotation.DefaultMyBatisMapper;
import com.github.regyl.gfi.entity.IssueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@DefaultMyBatisMapper
public interface IssueRepository {

    void saveAll(@Param("entities") List<IssueEntity> entities, @Param("tableName") String tableName);
}

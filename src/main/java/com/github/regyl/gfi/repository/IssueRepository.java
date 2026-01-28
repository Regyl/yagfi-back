package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.IssueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IssueRepository {

    void saveAll(@Param("entities") List<IssueEntity> entities, @Param("tableName") String tableName);
    @Select("SELECT url FROM gfi.e_issue_1 ORDER BY RANDOM() LIMIT 1")
    String findRandomIssueLink();
}

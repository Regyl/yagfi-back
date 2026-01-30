package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.entity.IssueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IssueRepository {

    void saveAll(@Param("entities") List<IssueEntity> entities, @Param("tableName") String tableName);

    String findRandomIssueLink(@Param("filters") DataRequestDto filters);
}

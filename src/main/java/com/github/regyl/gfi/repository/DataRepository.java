package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.IssueResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DataRepository {

    List<IssueResponseDto> findAllIssues(DataRequestDto requestDto);

    Collection<String> findAllLanguages();

    String findRandomIssueLink(DataRequestDto filters);
}

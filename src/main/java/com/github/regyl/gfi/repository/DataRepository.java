package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.dto.request.issue.DataRequestDto;
import com.github.regyl.gfi.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.dto.response.statistic.LabelStatisticResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DataRepository {

    List<IssueResponseDto> findAllIssues(DataRequestDto requestDto);

    Collection<String> findAllLanguages();

    Collection<String> findAllLicenses();

    String findRandomIssueLink(DataRequestDto filters);

    List<LabelStatisticResponseDto> findAllLabels();  

    List<String> findAllIssueLanguages();
}

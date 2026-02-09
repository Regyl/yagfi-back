package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.request.issue.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.issue.IssueResponseDto;
import com.github.regyl.gfi.controller.dto.response.statistic.LabelStatisticResponseDto;
import org.apache.ibatis.annotations.Mapper;
import java.util.Collection;
import java.util.List;

@Mapper
public interface DataRepository {

    List<IssueResponseDto> findAllIssues(DataRequestDto requestDto);

    Collection<String> findAllLanguages();

    String findRandomIssueLink(DataRequestDto filters);

    List<LabelStatisticResponseDto> findAllLabels();  

    List<String> findAllIssueLanguages();
}

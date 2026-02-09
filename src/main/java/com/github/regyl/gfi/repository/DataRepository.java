package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.IssueResponseDto;
import com.github.regyl.gfi.controller.dto.response.LabelStatisticResponseDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DataRepository {

    List<IssueResponseDto> findAllIssues(DataRequestDto requestDto);

    @Select("""
        SELECT language
        FROM gfi.issue_v
        WHERE language IS NOT NULL
        GROUP BY language
        ORDER BY COUNT(1) DESC
    """)
    Collection<String> findAllLanguages();

    String findRandomIssueLink(DataRequestDto filters);

    List<LabelStatisticResponseDto> findAllLabels();  

    


}

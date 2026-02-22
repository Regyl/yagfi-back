package com.github.regyl.gfi.service.other;

import com.github.regyl.gfi.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.dto.request.issue.DataRequestDto;
import com.github.regyl.gfi.dto.response.issue.DataResponseDto;
import com.github.regyl.gfi.dto.response.statistic.LabelStatisticResponseDto;
import com.github.regyl.gfi.model.IssueTables;

import java.util.Collection;
import java.util.List;

public interface DataService {

    void save(IssueDataDto response, IssueTables table);

    DataResponseDto findAllIssues(DataRequestDto requestDto);

    Collection<String> findAllLanguages();

    Collection<String> findAllLicenses();

    String findRandomIssueUrl(DataRequestDto filters);

    List<LabelStatisticResponseDto> findAllLabels();

    Collection<String> findAllIssueLanguages();
}

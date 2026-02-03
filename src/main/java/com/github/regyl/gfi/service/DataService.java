package com.github.regyl.gfi.service;

import com.github.regyl.gfi.controller.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.DataResponseDto;
import com.github.regyl.gfi.model.IssueTables;

import java.util.Collection;

public interface DataService {

    void save(IssueDataDto response, IssueTables table);

    DataResponseDto findAllIssues(DataRequestDto requestDto);

    Collection<String> findAllLanguages();
    String findRandomIssueUrl(DataRequestDto filters);
}

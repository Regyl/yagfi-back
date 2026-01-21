package com.github.regyl.gfi.service;

import com.github.regyl.gfi.controller.dto.github.GithubIssueData;
import com.github.regyl.gfi.controller.dto.gitlab.GitlabIssueData;
import com.github.regyl.gfi.controller.dto.request.DataRequestDto;
import com.github.regyl.gfi.controller.dto.response.DataResponseDto;
import com.github.regyl.gfi.model.IssueTables;

import java.util.Collection;

public interface DataService {

    void save(GithubIssueData response, IssueTables table);

    void save(GitlabIssueData response, IssueTables table);

    DataResponseDto findAllIssues(DataRequestDto requestDto);

    Collection<String> findAllLanguages();
}

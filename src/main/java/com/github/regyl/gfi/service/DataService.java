package com.github.regyl.gfi.service;

import com.github.regyl.gfi.controller.dto.github.IssueData;
import com.github.regyl.gfi.controller.dto.request.IssueRequestDto;
import com.github.regyl.gfi.entity.IssueEntity;
import org.springframework.data.domain.Page;

public interface DataService {

    void save(IssueData response);

    Page<IssueEntity> findAllIssues(IssueRequestDto requestDto);
}

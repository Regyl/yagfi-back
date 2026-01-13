package com.github.regyl.gfi.controller;

import com.github.regyl.gfi.controller.dto.request.IssueRequestDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.service.DataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/issues")
@CrossOrigin(origins = "http://localhost:3000") //TODO temporarily
public class IssuesController {

    private final DataService dataService;

    @PostMapping
    public Page<IssueEntity> findAll(@RequestBody @Valid IssueRequestDto requestDto) {
        return dataService.findAllIssues(requestDto);
    }
}

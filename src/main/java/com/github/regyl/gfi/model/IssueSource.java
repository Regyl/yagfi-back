package com.github.regyl.gfi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueSource {

    GITHUB("github"),
    GITLAB("gitlab"),

    ;

    private final String value;
}

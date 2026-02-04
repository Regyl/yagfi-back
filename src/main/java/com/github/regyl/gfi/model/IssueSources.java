package com.github.regyl.gfi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IssueSources {

    GITHUB("GitHub"),
    GITLAB("GitLab"),

    ;

    private final String name;
}

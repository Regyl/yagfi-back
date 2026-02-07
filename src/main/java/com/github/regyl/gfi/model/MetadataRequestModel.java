package com.github.regyl.gfi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MetadataRequestModel {

    private final String label;

    private final String dateFilter;
}

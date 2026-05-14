package com.github.regyl.gfi.model;

import com.github.regyl.gfi.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SbomModel {

    private UserFeedRequestEntity rq;

    private SbomResponseDto responseDto;

    private String repositoryUrl;
}

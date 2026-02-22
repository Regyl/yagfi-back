package com.github.regyl.gfi.dto.cyclonedx.sbom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SbomMetadataDto {

    private OffsetDateTime timestamp;
    private SbomToolsDto tools;
    private List<SbomAuthorDto> authors;
    private List<SbomLifecycleDto> lifecycles;
    private SbomComponentDto component;
    private List<SbomPropertyDto> properties;
    private String text;
}

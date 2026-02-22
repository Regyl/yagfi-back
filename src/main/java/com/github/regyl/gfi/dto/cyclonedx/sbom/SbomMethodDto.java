package com.github.regyl.gfi.dto.cyclonedx.sbom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SbomMethodDto {

    private String technique;
    private Double confidence;
    private String value;
}

package com.github.regyl.gfi.dto.cyclonedx.sbom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SbomIdentityDto {

    private String field;
    private Double confidence;
    private List<SbomMethodDto> methods;
    private String concludedValue;
}

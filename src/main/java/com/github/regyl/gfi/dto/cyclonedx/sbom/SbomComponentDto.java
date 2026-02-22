package com.github.regyl.gfi.dto.cyclonedx.sbom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SbomComponentDto {

    private String group;
    private String name;
    private String version;
    private String scope;
    private String purl;
    private String type;
    @JsonProperty("bom-ref")
    private String bomRef;
    private String publisher;
    private List<SbomAuthorDto> authors;
    private List<SbomPropertyDto> properties;
    private SbomEvidenceDto evidence;
    private List<String> tags;
}

package com.github.regyl.gfi.dto.external.ipinfo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpInfoResponseDto {

    private String ip;

    private String asn;

    @JsonProperty("as_name")
    private String asName;

    @JsonProperty("as_domain")
    private String asDomain;

    @JsonProperty("country_code")
    private String countryCode;

    private String country;

    @JsonProperty("continent_code")
    private String continentCode;

    private String continent;
}

package com.github.regyl.gfi.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequestDto {

    private LanguageFilterDto languages;
    private StarsFilterDto stars;
    private LicenseFilterDto licenses;
}

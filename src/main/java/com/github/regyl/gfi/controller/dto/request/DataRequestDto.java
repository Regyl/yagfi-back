package com.github.regyl.gfi.controller.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRequestDto {

    @Max(20)
    private Integer limit;
    private Integer offset;

    private FilterRequestDto filter;

    private Collection<OrderDto> orders;
    private List<String> languages;
}

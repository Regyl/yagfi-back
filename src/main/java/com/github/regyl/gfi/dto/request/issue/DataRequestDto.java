package com.github.regyl.gfi.dto.request.issue;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataRequestDto {

    @Max(20)
    private Integer limit;
    private Integer offset;

    private FilterRequestDto filter;

    private Collection<OrderDto> orders;
}

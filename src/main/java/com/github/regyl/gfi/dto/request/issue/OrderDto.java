package com.github.regyl.gfi.dto.request.issue;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    @NotNull
    private String field;

    @NotNull
    private String type;
}

package com.github.regyl.gfi.dto.request.feed;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserFeedRequestDto {

    @NotEmpty
    private String nickname;

    @Email
    @NotEmpty
    private String email;
}

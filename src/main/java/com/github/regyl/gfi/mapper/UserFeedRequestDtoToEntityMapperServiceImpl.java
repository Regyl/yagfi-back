package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.controller.dto.request.UserFeedRequestDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UserFeedRequestDtoToEntityMapperServiceImpl implements Function<UserFeedRequestDto, UserFeedRequestEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;

    @Override
    public UserFeedRequestEntity apply(UserFeedRequestDto dto) {
        return UserFeedRequestEntity.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .created(dateTimeSupplier.get())
                .build();
    }
}

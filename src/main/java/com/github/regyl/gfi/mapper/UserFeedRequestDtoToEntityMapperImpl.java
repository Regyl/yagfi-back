package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class UserFeedRequestDtoToEntityMapperImpl implements Function<UserFeedRequestDto, UserFeedRequestEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;

    @Override
    public UserFeedRequestEntity apply(UserFeedRequestDto dto) {
        return UserFeedRequestEntity.builder()
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .created(dateTimeSupplier.get())
                .status(UserFeedRequestStatuses.WAITING_FOR_PROCESS.getValue())
                .build();
    }
}

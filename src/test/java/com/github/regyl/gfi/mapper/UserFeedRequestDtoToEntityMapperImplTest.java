package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@DefaultUnitTest
class UserFeedRequestDtoToEntityMapperImplTest {

    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;

    @InjectMocks
    private UserFeedRequestDtoToEntityMapperImpl mapper;

    @Test
    @DisplayName("Null input throws NullPointerException")
    void apply_nullInput_returnsNull() {

        // Given
        UserFeedRequestDto nullDto = null;

        // When
        UserFeedRequestEntity result = mapper.apply(nullDto);

        // Then
        assertThat(result).isNull();
        verifyNoInteractions(dateTimeSupplier);
    }

    @Test
    @DisplayName("Fully filled DTO maps all fields correctly")
    void apply_fullyFilledDto_mapsAllFieldsCorrectly() {

        // Given
        final OffsetDateTime fixedTime = OffsetDateTime.of(2023, 10, 1, 12, 0, 0, 0, ZoneOffset.UTC);
        when(dateTimeSupplier.get()).thenReturn(fixedTime);

        UserFeedRequestDto dto = new UserFeedRequestDto();
        dto.setNickname("test_user");
        dto.setEmail("user@example.com");

        // When
        UserFeedRequestEntity result = mapper.apply(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNickname()).isEqualTo("test_user");
        assertThat(result.getEmail()).isEqualTo("user@example.com");
        assertThat(result.getCreated()).isEqualTo(fixedTime);
        assertThat(result.getStatus())
                .isEqualTo(UserFeedRequestStatuses.WAITING_FOR_PROCESS.getValue());
    }
}

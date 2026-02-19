
package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.controller.dto.request.feed.UserFeedRequestDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@DefaultUnitTest
class UserFeedRequestDtoToEntityMapperImplTest {

    @InjectMocks
    private UserFeedRequestDtoToEntityMapperImpl mapper;

    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;

    @Test
     void shouldReturnNull(){
        assertThat(mapper.apply(null)).isNull();
    }

    @Test
    void shouldReturnFullEntity() {
        UserFeedRequestDto dados = new UserFeedRequestDto();
        dados.setNickname("Luiz Felipe");
        dados.setEmail("luiz1234@gmail.com");

        OffsetDateTime now = OffsetDateTime.parse("2026-02-18T10:00:00Z");
        when(dateTimeSupplier.get()).thenReturn(now);

        UserFeedRequestEntity resultado = mapper.apply(dados);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNickname()).isEqualTo("Luiz Felipe");
        assertThat(resultado.getEmail()).isEqualTo("luiz1234@gmail.com");
        assertThat(resultado.getCreated()).isEqualTo(now);
        assertThat(resultado.getStatus())
                .isEqualTo(UserFeedRequestStatuses.WAITING_FOR_PROCESS.getValue());
    }
}

package com.github.regyl.gfi.mapper;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import com.github.regyl.gfi.dto.request.feed.UserFeedRequestDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserFeedRequestDtoToEntityMapperImplTest {

	@Test
	void shouldThrowExceptionWhenInputIsNull() {
		Supplier<OffsetDateTime> supplier = OffsetDateTime::now;
		UserFeedRequestDtoToEntityMapperImpl mapper = new UserFeedRequestDtoToEntityMapperImpl(supplier);

		assertThrows(NullPointerException.class, () -> mapper.apply(null));
	}

	@Test
	void shouldMapAllFieldsCorrectly() {
		Supplier<OffsetDateTime> supplier = OffsetDateTime::now;

		UserFeedRequestDtoToEntityMapperImpl mapper = new UserFeedRequestDtoToEntityMapperImpl(supplier);

		UserFeedRequestDto dto = new UserFeedRequestDto();
		dto.setNickname("testUser");
		dto.setEmail("user@test.com");

		var entity = mapper.apply(dto);

		assertNotNull(entity);
		assertEquals("testUser", entity.getNickname());
		assertEquals("user@test.com", entity.getEmail());
		assertNotNull(entity.getCreated());
		assertNotNull(entity.getStatus());
	}
}
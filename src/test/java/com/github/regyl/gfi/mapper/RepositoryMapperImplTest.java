package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.controller.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.entity.RepositoryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class RepositoryMapperImplTest {
    @InjectMocks
    private RepositoryMapperImpl repositoryMapper;
    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;
    @Test
     void resultNullTest() {
        RepositoryEntity result = repositoryMapper.apply(null);
        assertNull(result, "Mapping a null input should return null");
    }
    @Test
     void shouldMapFullDtoToRepositoryEntity() {
        GithubRepositoryDto dto = new GithubRepositoryDto();
        dto.setId("1");
        dto.setNameWithOwner("Regyl/project");
        dto.setUrl("https://github.com/Regyl/gfi");
        dto.setStargazerCount(100);
        dto.setDescription("Um projeto icrivel");
        OffsetDateTime now = OffsetDateTime.now();
        when(dateTimeSupplier.get()).thenReturn(now);
        RepositoryEntity resultado = repositoryMapper.apply(dto);
        assertNotNull(resultado, "The result must not be null.");
        assertEquals("1", resultado.getSourceId());
        assertEquals("Regyl/project", resultado.getTitle());
        assertEquals("https://github.com/Regyl/gfi", resultado.getUrl());
        assertEquals(100, resultado.getStars());
        assertEquals("Um projeto icrivel", resultado.getDescription());
        assertEquals(now, resultado.getCreated());
    }
}

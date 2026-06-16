package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.entity.RepositoryEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DefaultUnitTest
class RepositoryMapperImplTest {

    @InjectMocks
    private RepositoryMapperImpl repositoryMapper;

    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;

    @Test
    void shouldReturnNullWhenInputIsNull() {
        // When
        RepositoryEntity result = repositoryMapper.apply(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapFullDtoToRepositoryEntity() {
        // Given
        GithubRepositoryDto dto = new GithubRepositoryDto();
        dto.setId("1");
        dto.setNameWithOwner("Regyl/project");
        dto.setUrl("https://github.com/Regyl/gfi");
        dto.setStargazerCount(100);
        dto.setDescription("A great repository description");

        OffsetDateTime now = OffsetDateTime.now();
        when(dateTimeSupplier.get()).thenReturn(now);

        // When
        RepositoryEntity result = repositoryMapper.apply(dto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getSourceId()).isEqualTo("1");
        assertThat(result.getTitle()).isEqualTo("Regyl/project");
        assertThat(result.getUrl()).isEqualTo("https://github.com/Regyl/gfi");
        assertThat(result.getStars()).isEqualTo(100);
        assertThat(result.getDescription()).isEqualTo("A great repository description");
        assertThat(result.getCreated()).isEqualTo(now);
    }

}

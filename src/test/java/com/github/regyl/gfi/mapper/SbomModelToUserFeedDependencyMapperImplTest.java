package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.OffsetDateTime;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DefaultUnitTest
class SbomModelToUserFeedDependencyMapperImplTest {

    @Mock
    private Supplier<OffsetDateTime> dateTimeSupplier;

    @InjectMocks
    private SbomModelToUserFeedDependencyMapperImpl mapper;

    @Test
    void apply_nullInput_returnsNull() {
        // Given
        String dependencyUrl = "https://dependency.com";

        // When
        UserFeedDependencyEntity result = mapper.apply(null, dependencyUrl);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void apply_fullyFilledModel_mapsAllFields() {
        // Given
        Long requestId = 1L;
        String repositoryUrl = "https://github.com/test/repository";
        String dependencyUrl = "https://dependency.com";
        OffsetDateTime created = OffsetDateTime.now();

        UserFeedRequestEntity request = UserFeedRequestEntity.builder()
                .id(requestId)
                .nickname("test-user")
                .email("test@example.com")
                .status("ACTIVE")
                .build();

        SbomResponseDto responseDto = mock(SbomResponseDto.class);

        SbomModel model = new SbomModel(
                request,
                responseDto,
                repositoryUrl
        );

        when(dateTimeSupplier.get()).thenReturn(created);

        // When
        UserFeedDependencyEntity result = mapper.apply(model, dependencyUrl);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRequestId()).isEqualTo(requestId);
        assertThat(result.getSourceRepo()).isEqualTo(repositoryUrl);
        assertThat(result.getDependencyUrl()).isEqualTo(dependencyUrl);
        assertThat(result.getCreated()).isEqualTo(created);
    }
}


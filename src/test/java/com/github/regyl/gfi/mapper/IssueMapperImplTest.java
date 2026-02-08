package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.controller.dto.github.issue.GithubIssueDto;
import com.github.regyl.gfi.controller.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

@DefaultUnitTest
class IssueMapperImplTest {

    private IssueMapperImpl target;

    private final Supplier<OffsetDateTime> dateTimeSupplier =
            () -> OffsetDateTime.of(2026, 2, 6, 1, 20, 0, 0, ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        target = new IssueMapperImpl(dateTimeSupplier);
    }

    @ParameterizedTest
    @MethodSource("languages")
    void testLanguageDetection(String title, String expectedLanguage) {
        // given
        GithubIssueDto dto = new GithubIssueDto();
        dto.setTitle(title);

        // when
        IssueEntity result = target.apply(Map.of(), dto);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLanguage()).isEqualTo(expectedLanguage);
    }

    static Stream<Arguments> languages() {
        return Stream.of(
                Arguments.of("Some text", "ENGLISH"),
                Arguments.of(
                        "Claude: [CODE] MCPConnection 连接超时硬编码为 30000ms 应该可配置",
                        "CHINESE"
                )
        );
    }

    @Test
    void shouldMapAllFieldsWhenInputIsFullyFilled() {
        // given
        OffsetDateTime now = dateTimeSupplier.get();

        RepositoryEntity repositoryEntity = RepositoryEntity.builder()
                .id(42L)
                .build();

        Map<String, RepositoryEntity> repos = Map.of(
                "repo-id", repositoryEntity
        );

        GithubRepositoryDto repoDto = new GithubRepositoryDto();
        repoDto.setId("repo-id");

        GithubIssueDto dto = new GithubIssueDto();
        dto.setId(100L);
        dto.setTitle("Fix login bug");
        dto.setUrl("https://github.com/test/repo/issues/1");
        dto.setCreatedAt(now.minusDays(1));
        dto.setUpdatedAt(now);
        dto.setRepository(repoDto);
        dto.setConvertedLabels(Set.of("bug", "good first issue"));

        // when
        IssueEntity result = target.apply(repos, dto);

        // then
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getSourceId()).isEqualTo(100L);
        Assertions.assertThat(result.getTitle()).isEqualTo("Fix login bug");
        Assertions.assertThat(result.getUrl()).isEqualTo("https://github.com/test/repo/issues/1");
        Assertions.assertThat(result.getCreatedAt()).isEqualTo(dto.getCreatedAt());
        Assertions.assertThat(result.getUpdatedAt()).isEqualTo(dto.getUpdatedAt());
        Assertions.assertThat(result.getRepositoryId()).isEqualTo(42L);
        Assertions.assertThat(result.getLabels())
                .containsExactlyInAnyOrder("bug", "good first issue");
        Assertions.assertThat(result.getCreated()).isEqualTo(now);
        Assertions.assertThat(result.getLanguage()).isNotNull();
    }
}


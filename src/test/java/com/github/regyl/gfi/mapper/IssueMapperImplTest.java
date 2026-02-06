package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.annotation.DefaultUnitTest;
import com.github.regyl.gfi.controller.dto.github.issue.GithubIssueDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@DefaultUnitTest
class IssueMapperImplTest {

    private IssueMapperImpl target;

    private final Supplier<OffsetDateTime> dateTimeSupplier = () -> OffsetDateTime.of(2026, 2, 6, 1, 20, 0, 0, ZoneOffset.UTC);

    @BeforeEach
    void setUp() {
        target = new IssueMapperImpl(dateTimeSupplier);
    }

    @ParameterizedTest
    @MethodSource("languages")
    void testLanguageDetection(String title, String language) {
        Map<String, RepositoryEntity> repos = new HashMap<>();
        GithubIssueDto dto = new GithubIssueDto();
        dto.setTitle(title);

        IssueEntity result = target.apply(repos, dto);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getLanguage()).isEqualTo(language);
    }

    static Stream<Arguments> languages() {
        return Stream.of(
                Arguments.of("Some text", "ENGLISH"),
                Arguments.of("Claude: [CODE] MCPConnection 连接超时硬编码为 30000ms 应该可配置", "CHINESE")
        );
    }
}

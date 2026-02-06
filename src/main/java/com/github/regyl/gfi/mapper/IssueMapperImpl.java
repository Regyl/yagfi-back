package com.github.regyl.gfi.mapper;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.github.regyl.gfi.controller.dto.github.issue.GithubIssueDto;
import com.github.regyl.gfi.controller.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class IssueMapperImpl implements BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> {

    private static final LanguageDetector LANGUAGE_DETECTOR = LanguageDetectorBuilder.fromAllLanguages()
            .withLowAccuracyMode()
            .build();

    private final Supplier<OffsetDateTime> dateTimeSupplier;

    @Override
    public IssueEntity apply(Map<String, RepositoryEntity> repos, GithubIssueDto dto) {
        Language detectedLanguage = LANGUAGE_DETECTOR.detectLanguageOf(dto.getTitle());

        String language = detectedLanguage != Language.UNKNOWN
                        ? detectedLanguage.name()
                        : null;

        Long repositoryId = Optional.ofNullable(dto.getRepository())
                .map(GithubRepositoryDto::getId)
                .map(repos::get)
                .map(RepositoryEntity::getId)
                .orElse(null);

        return IssueEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .repositoryId(repositoryId)
                .labels(dto.getConvertedLabels())
                .created(dateTimeSupplier.get())
                .language(language)
                .build();
    }
}

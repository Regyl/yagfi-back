package com.github.regyl.gfi.mapper;

import com.github.pemistahl.lingua.api.Language;
import com.github.pemistahl.lingua.api.LanguageDetector;
import com.github.pemistahl.lingua.api.LanguageDetectorBuilder;
import com.github.regyl.gfi.controller.dto.github.issue.GithubIssueDto;
import com.github.regyl.gfi.controller.dto.github.issue.GithubLabelDto;
import com.github.regyl.gfi.entity.IssueEntity;
import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class IssueMapperServiceImpl
        implements BiFunction<Map<String, RepositoryEntity>, GithubIssueDto, IssueEntity> {

    private static final LanguageDetector LANGUAGE_DETECTOR =
            LanguageDetectorBuilder.fromAllLanguages().build();

    @Override
    public IssueEntity apply(
            Map<String, RepositoryEntity> repos,
            GithubIssueDto dto
    ) {
        List<String> labels = dto.getLabels().getNodes().stream()
                .map(GithubLabelDto::getName)
                .toList();

        Language detectedLanguage =
                LANGUAGE_DETECTOR.detectLanguageOf(dto.getTitle());

        String language =
                detectedLanguage != Language.UNKNOWN
                        ? detectedLanguage.name()
                        : null;

        return IssueEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getTitle())
                .url(dto.getUrl())
                .updatedAt(dto.getUpdatedAt())
                .createdAt(dto.getCreatedAt())
                .repositoryId(repos.get(dto.getRepository().getId()).getId())
                .labels(labels)
                .language(language)
                .build();
    }
}

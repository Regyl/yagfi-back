package com.github.regyl.gfi.mapper.github;

import com.github.regyl.gfi.controller.dto.github.GithubRepositoryDto;
import com.github.regyl.gfi.entity.RepositoryEntity;
import com.github.regyl.gfi.model.IssueSource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class GithubRepositoryMapperServiceImpl implements Function<GithubRepositoryDto, RepositoryEntity> {

    @Override
    public RepositoryEntity apply(GithubRepositoryDto dto) {
        String primaryLanguage = dto.getPrimaryLanguage() == null ? null : dto.getPrimaryLanguage().getName();
        return RepositoryEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getNameWithOwner())
                .url(dto.getUrl())
                .stars(dto.getStargazerCount())
                .language(primaryLanguage)
                .description(dto.getDescription())
                .source(IssueSource.GITHUB.getValue())
                .build();
    }
}

package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.controller.dto.github.GithubRepositoryDto;
import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class RepositoryMapperServiceImpl implements Function<GithubRepositoryDto, RepositoryEntity> {

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
                .build();
    }
}

package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.controller.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RepositoryMapperServiceImpl implements Function<GithubRepositoryDto, RepositoryEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;

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
                .created(dateTimeSupplier.get())
                .build();
    }
}

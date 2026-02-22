package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.dto.github.issue.GithubRepositoryDto;
import com.github.regyl.gfi.entity.RepositoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class RepositoryMapperImpl implements Function<GithubRepositoryDto, RepositoryEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;

    @Override
    public RepositoryEntity apply(GithubRepositoryDto dto) {
        if (dto == null) {
            return null;
        }
        String primaryLanguage = dto.getPrimaryLanguage() == null ? null : dto.getPrimaryLanguage().getName();
        String license = dto.getLicenseInfo() == null ? null : dto.getLicenseInfo().getName();
        return RepositoryEntity.builder()
                .sourceId(dto.getId())
                .title(dto.getNameWithOwner())
                .url(dto.getUrl())
                .stars(dto.getStargazerCount())
                .language(primaryLanguage)
                .description(dto.getDescription())
                .license(license)
                .created(dateTimeSupplier.get())
                .build();
    }
}

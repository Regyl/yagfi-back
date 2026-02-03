package com.github.regyl.gfi.mapper;

import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.SbomModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Component
@RequiredArgsConstructor
public class SbomModelToUserFeedDependencyMapperImpl implements BiFunction<SbomModel, String, UserFeedDependencyEntity> {

    private final Supplier<OffsetDateTime> dateTimeSupplier;

    @Override
    public UserFeedDependencyEntity apply(SbomModel model, String githubUrl) {
        UserFeedRequestEntity rq = model.getRq();
        String nickname = rq.getNickname();
        String sourceRepo = model.getRepositoryUrl();

        return UserFeedDependencyEntity.builder()
                .nickname(nickname)
                .sourceRepo(sourceRepo)
                .dependencyGithubUrl(githubUrl)
                .created(dateTimeSupplier.get())
                .build();
    }
}

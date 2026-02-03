package com.github.regyl.gfi.service.impl.feed.cyclonedx;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.controller.dto.cyclonedx.sbom.SbomComponentDto;
import com.github.regyl.gfi.controller.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.model.SbomModel;
import com.github.regyl.gfi.repository.UserFeedDependencyRepository;
import com.github.regyl.gfi.service.feed.cyclonedx.PurlToHomepageService;
import com.github.regyl.gfi.util.PurlUtil;
import com.github.regyl.gfi.util.ServicePredicateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class CycloneDxSbomConsumerImpl implements Consumer<SbomModel> {

    private final UserFeedDependencyRepository userFeedDependencyRepository;
    private final Collection<PurlToHomepageService> homepageServices;
    private final BiFunction<SbomModel, String, UserFeedDependencyEntity> dependencyMapper;

    @Override
    public void accept(SbomModel model) {
        List<UserFeedDependencyEntity> dependencies = extractDependencies(model);
        userFeedDependencyRepository.saveAll(dependencies);
    }

    private List<UserFeedDependencyEntity> extractDependencies(SbomModel model) {
        SbomResponseDto dto = model.getResponseDto();

        List<UserFeedDependencyEntity> dependencies = new ArrayList<>();
        for (SbomComponentDto component : dto.getComponents()) {
            PackageURL purl = PurlUtil.toPurl(component.getPurl());
            if (purl == null) {
                continue;
            }

            // Пробуем извлечь в зависимости от типа пакета
            PurlToHomepageService service = ServicePredicateUtil.getTargetService(homepageServices, purl);
            String githubUrl = service.apply(purl);
            if (githubUrl == null) {
                log.warn("Github url could not be resolved by purl {}", purl);
                continue;
            }

            UserFeedDependencyEntity entity = dependencyMapper.apply(model, githubUrl);
            dependencies.add(entity);
        }

        return dependencies;
    }
}

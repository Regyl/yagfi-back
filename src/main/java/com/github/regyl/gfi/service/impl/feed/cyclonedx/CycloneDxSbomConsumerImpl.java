package com.github.regyl.gfi.service.impl.feed.cyclonedx;

import com.github.packageurl.PackageURL;
import com.github.regyl.gfi.dto.cyclonedx.sbom.SbomComponentDto;
import com.github.regyl.gfi.dto.cyclonedx.sbom.SbomResponseDto;
import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import com.github.regyl.gfi.model.SbomModel;
import com.github.regyl.gfi.repository.UserFeedDependencyRepository;
import com.github.regyl.gfi.service.feed.PurlToHomepageService;
import com.github.regyl.gfi.util.PurlUtil;
import com.github.regyl.gfi.util.ServicePredicateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * Consumer that processes SBOM responses from CycloneDX services.
 * 
 * <p>Extracts dependencies from SBOM components, resolves PURLs to GitHub repository URLs,
 * and saves dependency relationships to the database.
 * 
 * <p>Only processes components that:
 * <ul>
 *   <li>Have valid PURLs</li>
 *   <li>Have a matching PurlToHomepageService implementation</li>
 *   <li>Can be resolved to a GitHub repository URL</li>
 * </ul>
 * 
 * <p>Exceptions are caught and logged to prevent breaking the async CompletableFuture chain.
 * This ensures one failed SBOM doesn't stop the entire feed generation process.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CycloneDxSbomConsumerImpl implements Consumer<SbomModel> {

    private final UserFeedDependencyRepository userFeedDependencyRepository;
    private final Collection<PurlToHomepageService> homepageServices;
    private final BiFunction<SbomModel, String, UserFeedDependencyEntity> dependencyMapper;

    /**
     * Processes SBOM model and extracts dependencies. Wraps execution in try-catch
     * to prevent exceptions from breaking the async CompletableFuture chain.
     * 
     * @param model SBOM model containing response and request context
     */
    @Override
    public void accept(SbomModel model) {
        try {
            accept0(model);
        } catch (Exception e) {
            String msg = String.format("Error processing SbomModel: %s", e.getMessage());
            log.error(msg, e);
        }
    }

    private void accept0(SbomModel model) {
        List<UserFeedDependencyEntity> dependencies = extractDependencies(model);
        if (CollectionUtils.isEmpty(dependencies)) {
            return;
        }

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

            Optional<PurlToHomepageService> optionalService = ServicePredicateUtil.getTargetServiceNullSafe(
                    homepageServices, purl
            );
            if (optionalService.isEmpty()) {
                continue;
            }

            PurlToHomepageService service = optionalService.get();
            String githubUrl = service.apply(purl);
            if (githubUrl == null) {
                log.debug("Github url could not be resolved by purl {}", purl);
                continue;
            }

            UserFeedDependencyEntity entity = dependencyMapper.apply(model, githubUrl);
            dependencies.add(entity);
        }

        return dependencies;
    }
}

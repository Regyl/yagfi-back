package com.github.regyl.gfi.service.impl.issueload.issuesource.github;

import com.github.regyl.gfi.entity.GitHubMetadataEntity;
import com.github.regyl.gfi.model.LabelModel;
import com.github.regyl.gfi.repository.GitHubMetadataRepository;
import com.github.regyl.gfi.service.issueload.issuesource.github.GithubQueryBuilderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Component
@RequiredArgsConstructor
public class GithubQueryBuilderServiceImpl implements GithubQueryBuilderService {

    private static final int GITHUB_HIDDEN_PAGINATION_LIMIT = 1000;

    @Qualifier("scrappingStartDate")
    private final Supplier<LocalDate> scrappingStartDate;
    private final Supplier<LocalDate> dateSupplier;
    private final GitHubMetadataRepository gitHubMetadataRepository;

    @Override
    public Collection<String> apply(LabelModel label) {
        Optional<GitHubMetadataEntity> optionalEntity = gitHubMetadataRepository.findByLabel(label.getTitle());
        if (optionalEntity.isEmpty()) {
            return getDefaultQuery(label);
        }

        GitHubMetadataEntity meta = optionalEntity.get();
        Integer issueCount = meta.getIssueCount();
        if (issueCount <= GITHUB_HIDDEN_PAGINATION_LIMIT) {
            return getDefaultQuery(label);
        }

        long issuesPerDay = Math.round(issueCount * 1d / 365);
        long daysPerQuery = Math.floorDiv(GITHUB_HIDDEN_PAGINATION_LIMIT, issuesPerDay);

        List<String> queries = new ArrayList<>();
        LocalDate start = scrappingStartDate.get();
        log.info("Scrapping from date: {}", start);
        LocalDate now = dateSupplier.get();
        while (true) {
            LocalDate end = start.plusDays(daysPerQuery);
            String query = getQuery(label, start, end);
            queries.add(query);
            start = end;

            if (now.isBefore(end)) {
                break;
            }
        }

        return queries;
    }

    private String getQuery(LabelModel label, LocalDate startDate, LocalDate endDate) {
        return String.format("is:issue is:open no:assignee label:\"%s\" created:%s..%s",
                label.getTitle(),
                startDate,
                endDate
        );
    }

    private Collection<String> getDefaultQuery(LabelModel label) {
        String query = String.format("is:issue is:open no:assignee label:\"%s\" created:>=%s",
                label.getTitle(),
                dateSupplier.get()
        );
        return Collections.singletonList(query);
    }
}

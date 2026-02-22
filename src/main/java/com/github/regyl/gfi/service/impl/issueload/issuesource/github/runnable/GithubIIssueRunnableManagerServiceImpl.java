package com.github.regyl.gfi.service.impl.issueload.issuesource.github.runnable;

import com.github.regyl.gfi.configuration.async.AutoUploadConfigurationProperties;
import com.github.regyl.gfi.dto.github.issue.IssueDataDto;
import com.github.regyl.gfi.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.model.IssueTables;
import com.github.regyl.gfi.service.github.GithubClientService;
import com.github.regyl.gfi.service.issueload.issuesource.github.runnable.RunnableManagerService;
import com.github.regyl.gfi.service.other.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
@RequiredArgsConstructor
public class GithubIIssueRunnableManagerServiceImpl implements RunnableManagerService {

    @Qualifier("issueLoadAsyncExecutor")
    private final Executor executor;
    private final AutoUploadConfigurationProperties configProps;

    //dependencies for runnable
    private final GithubClientService<IssueRequestDto, IssueDataDto> githubClient;
    private final DataService dataService;

    @Override
    public Collection<CompletableFuture<Void>> apply(IssueTables table, BlockingQueue<IssueRequestDto> queries) {
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        for (int i = 0; i < configProps.getCorePoolSize(); i++) {
            Runnable runnable = new GithubIssueRunnableImpl(table, queries, dataService, githubClient);
            CompletableFuture<Void> future = CompletableFuture.runAsync(runnable, executor);
            futures.add(future);
        }
        return futures;
    }
}

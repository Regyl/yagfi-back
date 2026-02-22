package com.github.regyl.gfi.service.issueload.issuesource.github.runnable;

import com.github.regyl.gfi.dto.request.issue.IssueRequestDto;
import com.github.regyl.gfi.model.IssueTables;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface RunnableManagerService
        extends BiFunction<IssueTables, BlockingQueue<IssueRequestDto>, Collection<CompletableFuture<Void>>> {
}

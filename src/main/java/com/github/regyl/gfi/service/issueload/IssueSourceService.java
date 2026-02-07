package com.github.regyl.gfi.service.issueload;

import com.github.regyl.gfi.model.IssueTables;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public interface IssueSourceService {

    Collection<CompletableFuture<Void>> upload(IssueTables table);

    void raiseUploadEvent();
}

package com.github.regyl.gfi.service.issueload;

import com.github.regyl.gfi.model.IssueTables;

public interface IssueSourceService {

    void upload(IssueTables table);

    void raiseUploadEvent();
}

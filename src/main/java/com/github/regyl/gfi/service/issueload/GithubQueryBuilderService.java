package com.github.regyl.gfi.service.issueload;

import com.github.regyl.gfi.model.LabelModel;

import java.util.Collection;
import java.util.function.Function;

public interface GithubQueryBuilderService extends Function<LabelModel, Collection<String>> {

}

package com.github.regyl.gfi.repository;

import java.util.Collection;

public interface BatchUpsertRepository<T> {

    void saveAll(Collection<T> entities);
}

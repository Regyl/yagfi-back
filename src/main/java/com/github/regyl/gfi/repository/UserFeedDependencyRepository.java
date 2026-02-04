package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.UserFeedDependencyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserFeedDependencyRepository {

    void saveAll(List<UserFeedDependencyEntity> entities);
}

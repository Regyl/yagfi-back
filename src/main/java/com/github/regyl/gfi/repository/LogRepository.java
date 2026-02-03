package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogRepository {

    void save(LogEntity entity);
}

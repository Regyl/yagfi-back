package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.annotation.DefaultMyBatisMapper;
import com.github.regyl.gfi.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

@DefaultMyBatisMapper
public interface LogRepository {

    void save(LogEntity entity);
}

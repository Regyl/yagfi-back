package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.EventEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface EventRepository {

    Optional<EventEntity> findBySource(@Param("source") String source);

    List<EventEntity> findAll();

    void insert(EventEntity event);

    void update(EventEntity event);
}

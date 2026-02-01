package com.github.regyl.gfi.repository;

import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.model.UserFeedRequestStatuses;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserFeedRequestRepository {

    void save(UserFeedRequestEntity entity);

    Optional<UserFeedRequestEntity> findOldestByStatus(String status);

    void updateStatusById(Long id, UserFeedRequestStatuses status);

    void resetProcessingRecords();

    void deleteById(Long id);
}

package com.github.regyl.gfi.service.impl.other;

import com.github.regyl.gfi.controller.dto.request.UserFeedRequestDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;
import com.github.regyl.gfi.repository.UserFeedRequestRepository;
import com.github.regyl.gfi.service.other.UserFeedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFeedServiceImpl implements UserFeedService {

    private final UserFeedRequestRepository userFeedRequestRepository;
    private final Function<UserFeedRequestDto, UserFeedRequestEntity> feedRequestMapper;

    @Override
    public UserFeedRequestEntity saveFeedRequest(UserFeedRequestDto feedRequestDto) {
        UserFeedRequestEntity entity = feedRequestMapper.apply(feedRequestDto);
        return userFeedRequestRepository.save(entity);
    }
}

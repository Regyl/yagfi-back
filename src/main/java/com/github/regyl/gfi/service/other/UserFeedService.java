package com.github.regyl.gfi.service.other;

import com.github.regyl.gfi.controller.dto.request.UserFeedRequestDto;
import com.github.regyl.gfi.entity.UserFeedRequestEntity;

public interface UserFeedService {

    UserFeedRequestEntity saveFeedRequest(UserFeedRequestDto feedRequestDto);
}

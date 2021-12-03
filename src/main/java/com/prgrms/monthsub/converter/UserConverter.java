package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.response.SeriesGetWithUserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public SeriesGetWithUserResponse userToSeriesGetWithUserResponse(User user) {
        return new SeriesGetWithUserResponse(
            user.getId(),
            user.getEmail(),
            user.getProfileImage(),
            user.getProfileIntroduce(),
            user.getNickname()
        );
    }

}

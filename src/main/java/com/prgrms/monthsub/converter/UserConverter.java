package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.response.SeriesOneWithUserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public SeriesOneWithUserResponse userToSeriesOneWithUserResponse(User user) {
        return new SeriesOneWithUserResponse(
            user.getId(),
            user.getEmail(),
            user.getProfileImage(),
            user.getProfileIntroduce(),
            user.getNickname()
        );
    }

}

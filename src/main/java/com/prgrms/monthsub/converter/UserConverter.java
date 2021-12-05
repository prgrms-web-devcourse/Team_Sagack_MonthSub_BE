package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.SeriesSubscribeList.SeriesOneWithUserResponse;
import com.prgrms.monthsub.dto.UserSignUp;
import com.prgrms.monthsub.service.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    static final int point = 0;

    private final PartService partService;

    @Autowired
    PasswordEncoder bCryptEncoder;

    public SeriesOneWithUserResponse userToSeriesOneWithUserResponse(User user) {
        return new SeriesOneWithUserResponse(
            user.getId(),
            user.getEmail(),
            user.getProfileImage(),
            user.getProfileIntroduce(),
            user.getNickname()
        );
    }

    public User UserSignUpRequestToEntity(UserSignUp.Request request) {
        return User.builder()
            .email(request.email())
            .nickname(request.nickName())
            .password(bCryptEncoder.encode(request.password()))
            .point(point)
            .username(request.userName())
            .part(partService.findByName("USER_GROUP"))
            .build();
    }

}

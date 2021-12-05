package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.UserSignUp;
import com.prgrms.monthsub.dto.response.SeriesOneWithUserResponse;
import com.prgrms.monthsub.service.PartService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    private final BCryptPasswordEncoder bCryptEncoder;

    private final PartService partService;

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
            .point(10000)
            .username(request.userName())
            .part(partService.findByName("USER_GROUP"))
            .build();
    }

}

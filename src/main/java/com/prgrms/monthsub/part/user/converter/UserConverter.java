package com.prgrms.monthsub.part.user.converter;

import com.prgrms.monthsub.common.config.S3;
import com.prgrms.monthsub.part.user.app.PartService;
import com.prgrms.monthsub.part.user.domain.User;
import com.prgrms.monthsub.part.user.dto.UserSignUp;
import com.prgrms.monthsub.series.series.dto.SeriesSubscribeList.SeriesOneWithUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {

    static final int point = 0;

    private final PartService partService;

    private final S3 s3;

    @Autowired
    PasswordEncoder bCryptEncoder;

    public SeriesOneWithUserResponse userToSeriesOneWithUserResponse(User user) {
        return new SeriesOneWithUserResponse(
            user.getId(),
            user.getEmail(),
            this.s3.getDomain() + "/" + user.getProfileKey(),
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

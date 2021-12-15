package com.prgrms.monthsub.module.part.user.converter;

import static com.prgrms.monthsub.module.part.user.domain.User.POINT;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.user.app.PartService;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.dto.UserMe;
import com.prgrms.monthsub.module.part.user.dto.UserMe.Response;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesOneWithUserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

  private final S3 s3;
  private final PartService partService;
  private final PasswordEncoder bCryptEncoder;

  public UserConverter(
    S3 s3,
    PartService partService,
    PasswordEncoder bCryptEncoder
  ) {
    this.partService = partService;
    this.s3 = s3;
    this.bCryptEncoder = bCryptEncoder;
  }

  public SeriesOneWithUserResponse toSeriesOneWithUser(User user) {
    String profileImage =
      user.getProfileKey() == null ? null : this.s3.getDomain() + "/" + user.getProfileKey();

    return new SeriesOneWithUserResponse(
      user.getId(),
      user.getEmail(),
      profileImage,
      user.getProfileIntroduce(),
      user.getNickname()
    );
  }

  public UserMe.Response toUserMe(User user) {
    String profileImage =
      user.getProfileKey() == null ? null : this.s3.getDomain() + "/" + user.getProfileKey();

    return new Response(
      user.getId(),
      user.getEmail(),
      user.getUsername(),
      user.getNickname(),
      profileImage,
      user.getProfileIntroduce(),
      user.getPart()
        .getName()
    );
  }

  public User toEntity(UserSignUp.Request request) {
    return User.builder()
      .email(request.email())
      .nickname(request.nickName())
      .password(this.bCryptEncoder.encode(request.password()))
      .point(POINT)
      .username(request.userName())
      .part(this.partService.findByName("USER_GROUP"))
      .build();
  }

}

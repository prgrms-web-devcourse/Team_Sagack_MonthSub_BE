package com.prgrms.monthsub.module.part.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserMe {

  @Schema(name = "UserMe.Response")
  public record Response(
    Long userId,
    String email,
    String userName,
    String nickName,
    String profileKey,
    String profileIntroduce,
    String group
  ) {}

}

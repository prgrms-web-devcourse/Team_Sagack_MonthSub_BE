package com.prgrms.monthsub.module.part.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class UserEdit {

  @Schema(name = "UserEdit.Request")
  public record Request(
    String nickName,
    String profileIntroduce
  ) {}

  @Schema(name = "UserEdit.Response")
  public record Response(
    Long userId
  ) {}
}

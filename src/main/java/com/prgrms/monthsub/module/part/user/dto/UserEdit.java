package com.prgrms.monthsub.module.part.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class UserEdit {

  @Schema(name = "UserEdit.Request")
  public record Request(
    @NotBlank(message = "닉네임이 비어있습니다.")
    String nickName,

    @NotBlank(message = "소개글이 비어있습니다.")
    String profileIntroduce
  ) {
  }

  @Schema(name = "UserEdit.Response")
  public record Response(
    Long userId
  ) {
  }

}

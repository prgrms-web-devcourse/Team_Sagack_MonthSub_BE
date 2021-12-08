package com.prgrms.monthsub.module.part.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class UserSignUp {

  @Schema(name = "UserSignUp.Request")
  public record Request(
    @NotBlank(message = "이메일이 비어있습니다.")
    String email,

    @NotBlank(message = "이름이 비어있습니다.")
    String userName,

    @NotBlank(message = "비밀번호가 비어있습니다.")
    String password,

    @NotBlank(message = "닉네임이 비어있습니다.")
    String nickName
  ) {}

  @Schema(name = "UserSignUp.Response")
  public record Response(
    Long userId
  ) {}

}

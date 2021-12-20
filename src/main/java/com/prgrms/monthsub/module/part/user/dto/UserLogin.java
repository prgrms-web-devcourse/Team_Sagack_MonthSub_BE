package com.prgrms.monthsub.module.part.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class UserLogin {

  @Schema(name = "UserLogin.Request")
  public record Request(
    @NotBlank(message = "이메일이 비어있습니다.")
    String email,

    @NotBlank(message = "비밀번호가 비어있습니다.")
    String password
  ) {
  }

  @Schema(name = "UserLogin.Response")
  public record Response(
    Long userId,
    String token,
    String username,
    String group
  ) {
  }

}

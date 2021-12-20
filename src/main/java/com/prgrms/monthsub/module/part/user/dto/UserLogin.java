package com.prgrms.monthsub.module.part.user.dto;

import com.prgrms.monthsub.module.part.user.dto.UserLogin.Request;
import com.prgrms.monthsub.module.part.user.dto.UserLogin.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface UserLogin permits Request, Response {

  @Schema(name = "Login.Request")
  record Request(
    @NotBlank(message = "이메일이 비어있습니다.")
    String email,

    @NotBlank(message = "비밀번호가 비어있습니다.")
    String password
  ) implements UserLogin {
    @Builder
    public Request {
    }
  }

  @Schema(name = "Login.Response")
  record Response(
    Long userId,
    String token,
    String email,
    String group
  ) implements UserLogin {
    @Builder
    public Response {
    }
  }

}

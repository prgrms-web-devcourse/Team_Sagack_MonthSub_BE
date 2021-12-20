package com.prgrms.monthsub.module.part.user.dto;

import com.prgrms.monthsub.module.part.user.dto.UserSignUp.Request;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface UserSignUp permits Request, Response {

  @Schema(name = "SignUp.Request")
  record Request(
    @NotBlank(message = "이메일이 비어있습니다.")
    String email,

    @NotBlank(message = "이름이 비어있습니다.")
    String userName,

    @NotBlank(message = "비밀번호가 비어있습니다.")
    String password,

    @NotBlank(message = "닉네임이 비어있습니다.")
    String nickName
  ) implements UserSignUp {
    @Builder
    public Request {
    }
  }

  @Schema(name = "SignUp.Response")
  record Response(
    Long userId
  ) implements UserSignUp {
    @Builder
    public Response {
    }
  }

}

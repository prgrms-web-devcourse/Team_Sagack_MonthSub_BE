package com.prgrms.monthsub.module.part.user.dto;

import com.prgrms.monthsub.module.part.user.dto.UserEdit.Request;
import com.prgrms.monthsub.module.part.user.dto.UserEdit.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

public sealed interface UserEdit permits Request, Response {

  @Schema(name = "UserEdit.Request")
  record Request(
    @NotBlank(message = "닉네임이 비어있습니다.")
    String nickName,

    @NotBlank(message = "소개글이 비어있습니다.")
    String profileIntroduce
  ) implements UserEdit {

    @Builder
    public Request {
    }
  }

  @Schema(name = "UserEdit.Response")
  record Response(
    Long userId
  ) implements UserEdit {

    @Builder
    public Response {
    }
  }

}

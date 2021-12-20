package com.prgrms.monthsub.module.part.user.dto;

import com.prgrms.monthsub.module.part.user.dto.UserMe.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public sealed interface UserMe permits Response {

  @Schema(name = "UserMe.Response")
  record Response(
    Long userId,
    String email,
    String userName,
    String nickName,
    String profileKey,
    String profileIntroduce,
    String group
  ) implements UserMe {

    @Builder
    public Response {
    }
  }

}

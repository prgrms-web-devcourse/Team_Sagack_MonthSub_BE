package com.prgrms.monthsub.module.part.user.dto;

import com.prgrms.monthsub.module.part.user.dto.AccountEmail.Request;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;

public sealed interface AccountEmail permits Request {

  @Schema(name = "AccountEmail.Request")
  record Request(
    @NotBlank(message = "이메일을 입력해주세요.")
    @Pattern(regexp = "\\b[\\w.-]+@[\\w.-]+\\.\\w{2,4}\\b", message = "올바른 이메일 형식이 아닙니다.")
    String email
  ) implements AccountEmail {
    @Builder
    public Request {
    }
  }

}

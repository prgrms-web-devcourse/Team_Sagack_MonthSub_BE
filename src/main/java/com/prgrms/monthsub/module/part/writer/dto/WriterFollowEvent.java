package com.prgrms.monthsub.module.part.writer.dto;

import com.prgrms.monthsub.module.part.writer.dto.WriterFollowEvent.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

public sealed interface WriterFollowEvent permits Response {

  @Schema(name = "WriterFollowEvent.Response")
  record Response(
    Long userId,
    String followStatus
  ) implements WriterFollowEvent {

    @Builder
    public Response {
    }
  }

}

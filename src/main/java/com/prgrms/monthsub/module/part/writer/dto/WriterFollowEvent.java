package com.prgrms.monthsub.module.part.writer.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class WriterFollowEvent {

  @Schema(name = "WriterFollowEvent.Response")
  public record Response(
    Long id,
    String followStatus
  ) {
  }

}

package com.prgrms.monthsub.module.part.writer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public class WriterList {

  @Schema(name = "WriterList.Response")
  public record Response(
    List<WriterRes> popularWriterList
  ) {
  }

  @Schema(name = "WriterList.WriterRes")
  public record WriterRes(
    Long userId,
    Long writerId,
    String nickname,
    String profileImage,
    String subscribeStatus
  ) {
  }

}

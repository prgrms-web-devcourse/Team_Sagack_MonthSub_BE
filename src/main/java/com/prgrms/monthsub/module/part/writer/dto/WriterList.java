package com.prgrms.monthsub.module.part.writer.dto;

import com.prgrms.monthsub.module.part.writer.dto.WriterList.Response;
import com.prgrms.monthsub.module.part.writer.dto.WriterList.WriterResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public sealed interface WriterList permits Response, WriterResponse {

  @Schema(name = "WriterList.Response")
  record Response(
    List<WriterResponse> popularWriterList
  ) implements WriterList {
    @Builder
    public Response {
    }
  }

  @Schema(name = "WriterList.WriterRes")
  record WriterResponse(
    Long userId,
    Long writerId,
    String nickname,
    String profileImage,
    String subscribeStatus
  ) implements WriterList {
    @Builder
    public WriterResponse {
    }
  }

}

package com.prgrms.monthsub.module.part.writer.dto;

import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList.LikesResponse;
import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList.Response;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public sealed interface WriterLikesList permits Response, LikesResponse {

  @Schema(name = "WriterLikes.Response")
  record Response(
    List<LikesResponse> writerLikesList
  ) implements WriterLikesList {
    @Builder
    public Response {
    }
  }

  @Schema(name = "WriterLikesList.LikesResponse")
  record LikesResponse(
    Long writerLikesId,
    Long writerId,
    int followCount,
    String nickname,
    String profileImage,
    String profileIntroduce
  ) implements WriterLikesList {
    @Builder
    public LikesResponse {
    }
  }

}

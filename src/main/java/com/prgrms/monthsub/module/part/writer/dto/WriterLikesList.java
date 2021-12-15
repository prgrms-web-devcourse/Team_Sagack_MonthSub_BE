package com.prgrms.monthsub.module.part.writer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class WriterLikesList {

  @Schema(name = "WriterLikes.Response")
  public record Response(
    List<WriterLikesObject> writerLikesList
  ) {}

  @Getter
  @Builder
  public static class WriterLikesObject {
    public int followCount;
    public String nickname;
    public String profileKey;
    public String profileIntroduce;
  }

}

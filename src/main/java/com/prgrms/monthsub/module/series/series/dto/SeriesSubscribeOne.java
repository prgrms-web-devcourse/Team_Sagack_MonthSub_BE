package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.BriefArticleResponse;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SubscribeObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.UploadObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.WriterObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne.UsageEditResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public sealed interface SeriesSubscribeOne permits Response, UsageEditResponse {

  @Schema(name = "SeriesSubscribeOne.Response")
  record Response(
    Boolean isLiked,
    Boolean isMine,
    SeriesSubscribeList.SeriesObject series,
    UploadObject upload,
    SubscribeObject subscribe,
    Category category,
    WriterObject writer,
    List<BriefArticleResponse> articleList
  ) implements SeriesSubscribeOne {
    @Builder
    public Response {
    }
  }

  @Schema(name = "SeriesSubscribeOne.ResponseUsageEdit")
  record UsageEditResponse(
    SeriesSubscribeList.SeriesObject series,
    Category category,
    UploadObject upload,
    SubscribeObject subscribe
  ) implements SeriesSubscribeOne {
    @Builder
    public UsageEditResponse {
    }
  }

}

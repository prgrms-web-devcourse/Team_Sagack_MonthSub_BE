package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.BriefArticleResponse;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesListObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesUserResponse;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesWriterResponse;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SubscribeObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.UploadObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.WriterObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

public sealed interface SeriesSubscribeList permits Response, SeriesUserResponse,
  SeriesWriterResponse, BriefArticleResponse, SeriesListObject, SeriesObject,
  UploadObject, SubscribeObject, WriterObject {

  @Schema(name = "SeriesSubscribeList.Response")
  record Response(
    List<SeriesListObject> seriesList
  ) implements SeriesSubscribeList {
    @Builder
    public Response {
    }
  }

  @Schema(name = "SeriesSubscribeList.SeriesUserResponse")
  record SeriesUserResponse(
    Long userId,
    Long writerId,
    String email,
    String profileImage,
    String profileIntroduce,
    String nickname
  ) implements SeriesSubscribeList {
    @Builder
    public SeriesUserResponse {
    }
  }

  @Schema(name = "SeriesSubScribeList.SeriesWriterResponse")
  record SeriesWriterResponse(
    Long writerId,
    int followCount,
    SeriesUserResponse user
  ) implements SeriesSubscribeList {
    @Builder
    public SeriesWriterResponse {
    }
  }

  @Schema(name = "SeriesSubScribeList.BriefArticleResponse")
  record BriefArticleResponse(
    Long articleId,
    String title,
    Integer round,
    LocalDate date
  ) implements SeriesSubscribeList {
    @Builder
    public BriefArticleResponse {
    }
  }

  @Schema(name = "SeriesSubScribeList.SeriesListObject")
  record SeriesListObject(
    Boolean isLiked,
    Long userId,
    Long writerId,
    Long seriesId,
    String nickname,
    String thumbnail,
    String title,
    String introduceSentence,
    LocalDate seriesStartDate,
    LocalDate seriesEndDate,
    String subscribeStatus,
    LocalDate subscribeStartDate,
    LocalDate subscribeEndDate,
    int likes,
    Series.Category category
  ) implements SeriesSubscribeList {
    @Builder
    public SeriesListObject {
    }
  }

  @Schema(name = "SeriesSubScribeList.SeriesObject")
  record SeriesObject(
    Long id,
    String thumbnail,
    String title,
    String introduceText,
    String introduceSentence,
    int price,
    LocalDate startDate,
    LocalDate endDate,
    int articleCount,
    int likes,
    LocalDate createdDate,
    LocalDate updatedDate
  ) implements SeriesSubscribeList {
    @Builder
    public SeriesObject {
    }
  }

  @Schema(name = "SeriesSubScribeList.UploadObject")
  record UploadObject(
    String[] date,
    String time
  ) implements SeriesSubscribeList {
    @Builder
    public UploadObject {
    }
  }

  @Schema(name = "SeriesSubScribeList.SubscribeObject")
  record SubscribeObject(
    LocalDate startDate,
    LocalDate endDate,
    String status
  ) implements SeriesSubscribeList {
    @Builder
    public SubscribeObject {
    }
  }

  @Schema(name = "SeriesSubScribeList.WriterObject")
  record WriterObject(
    Long id,
    Long userId,
    int followCount,
    String email,
    String profileImage,
    String profileIntroduce,
    String nickname
  ) implements SeriesSubscribeList {
    @Builder
    public WriterObject {
    }
  }

}



package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

public class SeriesSubscribeList {

  @Schema(name = "SeriesSubscribeList.Response")
  public record Response(
    List<SeriesListObject> seriesList
  ) {
  }

  @Schema(name = "SeriesSubscribeList.SeriesOneWithUserResponse")
  public record SeriesOneWithUserResponse(
    Long userId,
    Long writerId,
    String email,
    String profileImage,
    String profileIntroduce,
    String nickname
  ) {
  }

  @Schema(name = "SeriesSubScribeList.SeriesOneWithWriterResponse")
  public record SeriesOneWithWriterResponse(
    Long writerId,
    int followCount,
    SeriesOneWithUserResponse user
  ) {
  }

  @Schema(name = "SeriesSubScribeList.BriefArticleBySeriesIdResponse")
  public record BriefArticleResponse(
    Long articleId,
    String title,
    Integer round,
    LocalDate date
  ) {
  }

  @Getter
  @Builder
  @Accessors(fluent = true, prefix = "is")
  public static class SeriesListObject {
    public Boolean isLiked;
    public Long userId;
    public Long writerId;
    public Long seriesId;
    public String nickname;
    public String thumbnail;
    public String title;
    public String introduceSentence;
    public LocalDate seriesStartDate;
    public LocalDate seriesEndDate;
    public String subscribeStatus;
    public LocalDate subscribeStartDate;
    public LocalDate subscribeEndDate;
    public int likes;
    public Series.Category category;
  }

  @Builder
  public static class SeriesObject {
    public Long id;
    public String thumbnail;
    public String title;
    public String introduceText;
    public String introduceSentence;
    public int price;
    public LocalDate startDate;
    public LocalDate endDate;
    public int articleCount;
    public int likes;
    public LocalDate createdDate;
    public LocalDate updatedDate;
  }

  @Builder
  public static class UploadObject {
    public String[] date;
    public String time;
  }

  @Builder
  public static class SubscribeObject {
    public LocalDate startDate;
    public LocalDate endDate;
    public String status;
  }

  @Builder
  public static class WriterObject {
    public Long id;
    public Long userId;
    public int followCount;
    public String email;
    public String profileImage;
    public String profileIntroduce;
    public String nickname;
  }

}



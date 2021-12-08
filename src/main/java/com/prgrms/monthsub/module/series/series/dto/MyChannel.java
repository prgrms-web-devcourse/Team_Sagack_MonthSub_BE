package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesOneWithUserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class MyChannel {

  @Schema(name = "MyChannel.Response")
  public record Response(
    SeriesOneWithUserResponse user,
    int followIngCount,
    List<MyChannelFollowWriterObject> followWriterList,
    List<MyChannelLikeObject> likeList,
    List<MyChannelSubscribeObject> subscribeList,
    int followCount,
    List<MyChannelSeriesObject> seriesPostList
  ) {
  }

  @Builder
  @Getter
  public static class MyChannelFollowWriterObject {
    Long id;
    String profileImage;
    String seriesStatus;
  }

  @Builder
  @Getter
  public static class MyChannelLikeObject {
    public Long id;
    public String thumbnail;
    public String title;
    public String introduceSentence;
    public LocalDate startDate;
    public LocalDate endDate;
    public int likes;
    Series.Category category;
  }

  @Builder
  @Getter
  public static class MyChannelSubscribeObject {
    public Long id;
    public String thumbnail;
    public String title;
    public String introduceSentence;
    public LocalDate startDate;
    public LocalDate endDate;
    public int likes;
    Series.Category category;
  }

  @Builder
  @Getter
  public static class MyChannelSeriesObject {
    public Long id;
    public String thumbnail;
    public String title;
    public String introduceSentence;
    public LocalDate startDate;
    public LocalDate endDate;
    public int likes;
    Series.Category category;
  }

}

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
    List<MyChannelSubscribeObject> subscribeList,
    int followCount,
    List<MyChannelSeriesObject> seriesPostList
  ) {
  }

  @Schema(name = "MyChannel.OtherResponse")
  public record OtherResponse(
    SeriesOneWithUserResponse user,
    int followIngCount,
    List<MyChannelFollowWriterObject> followWriterList,
    int followCount,
    List<MyChannelSeriesObject> seriesPostList
  ) {
  }

  @Builder
  @Getter
  public static class MyChannelFollowWriterObject {
    Long userId;
    Long writerId;
    String nickname;
    String profileImage;
    String subscribeStatus;
  }

  @Builder
  @Getter
  public static class MyChannelSubscribeObject {
    public boolean likeStatus;
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
    Series.Category category;
  }

  @Builder
  @Getter
  public static class MyChannelSeriesObject {
    public boolean likeStatus;
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
    Series.Category category;
  }

}

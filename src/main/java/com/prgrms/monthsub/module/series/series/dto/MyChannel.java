package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesOneWithUserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

public class MyChannel {

  @Schema(name = "MyChannel.Response")
  public record Response(
    Boolean isFollowed,
    Boolean isMine,
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
    Boolean isFollowed,
    Boolean isMine,
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
  @Accessors(fluent = true, prefix = "is")
  public static class MyChannelSubscribeObject {
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
    Series.Category category;
  }

  @Builder
  @Getter
  @Accessors(fluent = true, prefix = "is")
  public static class MyChannelSeriesObject {
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
    Series.Category category;
  }

}

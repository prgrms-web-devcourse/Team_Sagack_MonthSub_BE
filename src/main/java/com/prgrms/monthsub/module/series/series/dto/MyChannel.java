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
    List<MyChannel.FollowWriterObject> followWriterList,
    List<MyChannel.SubscribeObject> subscribeList,
    int followCount,
    List<MyChannel.SeriesObject> seriesPostList
  ) {
  }

  @Schema(name = "MyChannel.OtherResponse")
  public record OtherResponse(
    Boolean isFollowed,
    Boolean isMine,
    SeriesOneWithUserResponse user,
    int followIngCount,
    List<MyChannel.FollowWriterObject> followWriterList,
    int followCount,
    List<MyChannel.SeriesObject> seriesPostList
  ) {
  }

  @Builder
  @Getter
  public static class FollowWriterObject {
    Long userId;
    Long writerId;
    String nickname;
    String profileImage;
    String subscribeStatus;
  }

  @Builder
  @Getter
  @Accessors(fluent = true, prefix = "is")
  public static class SubscribeObject {
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
  @Getter
  @Accessors(fluent = true, prefix = "is")
  public static class SeriesObject {
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

}

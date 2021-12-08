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
    int followIngCount, //내가 팔로우한 작가 수
    List<MyChannelFollowWriterObject> followWriterList,
    List<MyChannelLikeObject> likeList,
    List<MyChannelSubscribeObject> subscribeList,
    // 작가일때 + 내가 발행한 시리즈 리스트
    int followCount,
    List<MyChannelSeriesObject> seriesPostList
  ) {

  }

  @Builder
  @Getter
  public static class MyChannelFollowWriterObject { //유저 - 내가 팔로우한 작가 정보

    Long id; //작가 아이디

    String profileImage;

    String seriesStatus; //현재 모집중인 시리즈의 상태 정보

  }


  @Builder
  @Getter
  public static class MyChannelLikeObject { //유저 - 내가 좋아요한 시리즈 정보

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
  public static class MyChannelSubscribeObject { //유저 - 내가 구독한 시리즈

    public Long id;

    public String thumbnail;

    public String title;

    public String introduceSentence;

    public LocalDate startDate;

    public LocalDate endDate;

    public int likes;

    Series.Category category;

  }

  // + 작가일때 -> 내가 작성한 리스트 정보
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

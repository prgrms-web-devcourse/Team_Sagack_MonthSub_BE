package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.FollowWriterObject;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.OtherResponse;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.Response;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.SeriesObject;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.SubscribeObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesUserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;

public sealed interface MyChannel permits Response, OtherResponse, FollowWriterObject,
  SubscribeObject, SeriesObject {

  @Schema(name = "MyChannel.Response")
  record Response(
    Boolean isFollowed,
    Boolean isMine,
    SeriesUserResponse user,
    int followIngCount,
    List<MyChannel.FollowWriterObject> followWriterList,
    List<MyChannel.SubscribeObject> subscribeList,
    int followCount,
    List<MyChannel.SeriesObject> seriesPostList
  ) implements MyChannel {
    @Builder
    public Response {
    }
  }

  @Schema(name = "MyChannel.OtherResponse")
  record OtherResponse(
    Boolean isFollowed,
    Boolean isMine,
    SeriesUserResponse user,
    int followIngCount,
    List<MyChannel.FollowWriterObject> followWriterList,
    int followCount,
    List<MyChannel.SeriesObject> seriesPostList
  ) implements MyChannel {
    @Builder
    public OtherResponse {
    }
  }

  @Schema(name = "MyChannel.FollowWriterObject")
  record FollowWriterObject(
    Long userId,
    Long writerId,
    String nickname,
    String profileImage,
    String subscribeStatus
  ) implements MyChannel {
    @Builder
    public FollowWriterObject {
    }
  }

  @Schema(name = "MyChannel.SubscribeObject")
  record SubscribeObject(
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
  ) implements MyChannel {
    @Builder
    public SubscribeObject {
    }
  }

  @Schema(name = "MyChannel.SeriesObject")
  record SeriesObject(
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
  ) implements MyChannel {
    @Builder
    public SeriesObject {
    }
  }

}

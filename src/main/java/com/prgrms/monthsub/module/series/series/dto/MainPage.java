package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.Series;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

public class MainPage {

  @Schema(name = "MainPage.Response")
  public record Response(
    List<MainPageSubscribeObject> popularSeriesList,
    List<MainPageFollowWriterObject> popularWriterList,
    List<MainPageSubscribeObject> recentSeriesList
  ) {
  }

  @Getter
  @Builder
  public static class MainPageFollowWriterObject {
    Long userId;
    Long writerId;
    String nickname;
    String profileImage;
    String subscribeStatus;
  }

  @Getter
  @Builder
  @Accessors(fluent = true, prefix = "is")
  public static class MainPageSubscribeObject {
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

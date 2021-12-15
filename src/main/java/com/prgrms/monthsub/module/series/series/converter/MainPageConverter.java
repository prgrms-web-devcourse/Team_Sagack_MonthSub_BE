package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.MainPage;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MainPageConverter {

  private final WriterConverter writerConverter;
  private final S3 s3;

  public MainPageConverter(
    WriterConverter writerConverter,
    S3 s3
  ) {
    this.writerConverter = writerConverter;
    this.s3 = s3;
  }

  public MainPage.Response toResponse(
    List<Series> popularSeriesList,
    List<Writer> popularWriterList,
    List<Series> recentSeriesList
  ) {
    return new MainPage.Response(
      popularSeriesList.stream()
        .map(this::toMainPageSubscribeObject)
        .collect(Collectors.toList()),
      popularWriterList.stream()
        .map(this.writerConverter::toMainPageFollowWriterObject)
        .collect(Collectors.toList()),
      recentSeriesList.stream()
        .map(this::toMainPageSubscribeObject)
        .collect(Collectors.toList())
    );
  }

  public MainPage.MainPageSubscribeObject toMainPageSubscribeObject(
    Series series
  ) {
    return MainPage.MainPageSubscribeObject.builder()
      .userId(series.getWriter().getUser().getId())
      .writerId(series.getWriter().getId())
      .nickname(series.getWriter().getUser().getNickname())
      .seriesId(series.getId())
      .thumbnail(this.s3.getDomain() + "/" + series.getThumbnailKey())
      .title(series.getTitle())
      .subscribeStatus(String.valueOf(series.getSubscribeStatus()))
      .introduceSentence(series.getIntroduceSentence())
      .seriesStartDate(series.getSeriesStartDate())
      .seriesEndDate(series.getSeriesEndDate())
      .subscribeStartDate(series.getSeriesStartDate())
      .subscribeEndDate(series.getSubscribeEndDate())
      .likes(series.getLikes())
      .category(series.getCategory())
      .build();
  }

}

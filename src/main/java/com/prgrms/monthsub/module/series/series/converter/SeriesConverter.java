package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesOneWithWriterResponse;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SubscribeObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.UploadObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.WriterObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne.ResponseUsageEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SeriesConverter {

  private static final int DEFAULT_LIKES = 0;

  private final ArticleConverter articleConverter;
  private final WriterConverter writerConverter;
  private final S3 s3;

  public SeriesConverter(
    ArticleConverter articleConverter,
    WriterConverter writerConverter,
    S3 s3
  ) {
    this.articleConverter = articleConverter;
    this.writerConverter = writerConverter;
    this.s3 = s3;
  }

  public Series SeriesSubscribePostResponseToEntity(
    Writer writer,
    SeriesSubscribePost.Request req
  ) {
    return Series.builder()
      .title(req.title())
      .introduceText(req.introduceText())
      .introduceSentence(req.introduceSentence())
      .price(req.price())
      .subscribeStartDate(LocalDate.parse(req.subscribeStartDate()))
      .subscribeEndDate(LocalDate.parse(req.subscribeEndDate()))
      .seriesStartDate(LocalDate.parse(req.seriesStartDate()))
      .seriesEndDate(LocalDate.parse(req.seriesEndDate()))
      .articleCount(req.articleCount())
      .subscribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE)
      .likes(DEFAULT_LIKES)
      .category(Category.of(req.category()))
      .uploadTime(LocalTime.parse(req.uploadTime()))
      .writer(writer)
      .build();
  }

  public SeriesSubscribeOne.Response seriesToSeriesOneResponse(
    Series series,
    List<Article> articleList,
    List<ArticleUploadDate> uploadDateList
  ) {

    SeriesOneWithWriterResponse writerResponse = writerConverter.writerToSeriesOneWithWriterResponse(
      series.getWriter());
    return new Response(
      SeriesObject.builder()
        .id(series.getId())
        .thumbnail(this.toThumbnailEndpoint(series.getThumbnailKey()))
        .title(series.getTitle())
        .introduceText(series.getIntroduceText())
        .introduceSentence(series.getIntroduceSentence())
        .price(series.getPrice())
        .startDate(series.getSeriesStartDate())
        .endDate(series.getSeriesEndDate())
        .articleCount(series.getArticleCount())
        .likes(series.getLikes())
        .build(),
      UploadObject.builder()
        .date(uploadDateList.stream()
          .map(
            uploadDate -> {
              return uploadDate.getUploadDate()
                .toString()
                .toLowerCase();
            }
          )
          .toArray(String[]::new))
        .time(series.getUploadTime()
          .toString())
        .build(),
      SubscribeObject.builder()
        .startDate(series.getSubscribeStartDate())
        .endDate(series.getSubscribeEndDate())
        .status(String.valueOf(series.getSubscribeStatus()))
        .build(),
      series.getCategory(),
      WriterObject.builder()
        .id(writerResponse.writerId())
        .userId(writerResponse.user()
          .userId())
        .followCount(writerResponse.followCount())
        .email(writerResponse.user()
          .email())
        .profileImage(writerResponse.user()
          .profileImage())
        .profileIntroduce(writerResponse.user()
          .profileIntroduce())
        .nickname(writerResponse.user()
          .nickname())
        .build(),
      articleList.stream()
        .map(articleConverter::articleToArticleBySeriesIdResponse)
        .collect(Collectors.toList())
    );
  }

  public SeriesSubscribeList.SeriesListObject seriesListToResponse(Series series) {
    return SeriesSubscribeList.SeriesListObject.builder()
      .userId(series.getWriter()
        .getUser()
        .getId())
      .writerId(series.getWriter()
        .getId())
      .nickname(series.getWriter()
        .getUser()
        .getNickname())
      .seriesId(series.getId())
      .thumbnail(this.toThumbnailEndpoint(series.getThumbnailKey()))
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

  public SeriesSubscribeOne.ResponseUsageEdit seriesToResponseUsageEdit(
    Series series,
    List<ArticleUploadDate> uploadDateList
  ) {
    return new ResponseUsageEdit(
      SeriesObject.builder()
        .id(series.getId())
        .title(series.getTitle())
        .introduceSentence(series.getIntroduceSentence())
        .thumbnail(this.toThumbnailEndpoint(series.getThumbnailKey()))
        .price(series.getPrice())
        .build(),
      series.getCategory(),
      UploadObject.builder()
        .date(uploadDateList.stream()
          .map(
            uploadDate -> {
              return uploadDate.getUploadDate()
                .toString()
                .toLowerCase();
            }
          )
          .toArray(String[]::new))
        .time(series.getUploadTime()
          .toString())
        .build(),
      SubscribeObject.builder()
        .startDate(series.getSubscribeStartDate())
        .endDate(series.getSubscribeEndDate())
        .status(String.valueOf(series.getSubscribeStatus()))
        .build()
    );
  }

  public MyChannel.MyChannelLikeObject seriesToMyChannelLikeObject(Series series) {
    return MyChannel.MyChannelLikeObject.builder()
      .userId(series.getWriter()
        .getUser()
        .getId())
      .writerId(series.getWriter()
        .getId())
      .nickname(series.getWriter()
        .getUser()
        .getNickname())
      .seriesId(series.getId())
      .thumbnail(this.toThumbnailEndpoint(series.getThumbnailKey()))
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

  public MyChannel.MyChannelSubscribeObject seriesToMyChannelSubscribeObject(Series series) {
    return MyChannel.MyChannelSubscribeObject.builder()
      .userId(series.getWriter()
        .getUser()
        .getId())
      .writerId(series.getWriter()
        .getId())
      .nickname(series.getWriter()
        .getUser()
        .getNickname())
      .seriesId(series.getId())
      .thumbnail(this.toThumbnailEndpoint(series.getThumbnailKey()))
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

  public MyChannel.MyChannelSeriesObject seriesToMyChannelSeriesObject(Series series) {
    return MyChannel.MyChannelSeriesObject.builder()
      .userId(series.getWriter()
        .getUser()
        .getId())
      .writerId(series.getWriter()
        .getId())
      .nickname(series.getWriter()
        .getUser()
        .getNickname())
      .seriesId(series.getId())
      .thumbnail(this.toThumbnailEndpoint(series.getThumbnailKey()))
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

  public String toThumbnailEndpoint(String thumbnailKey) {
    return this.s3.getDomain() + "/" + thumbnailKey;
  }

}

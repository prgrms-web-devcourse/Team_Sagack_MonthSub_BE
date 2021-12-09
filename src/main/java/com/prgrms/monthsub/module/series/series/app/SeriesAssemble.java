package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.article.app.ArticleService;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageName;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageStatus;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SeriesAssemble {

  private final SeriesService seriesService;
  private final ArticleService articleService;
  private final ExpulsionService expulsionService;
  private final WriterProvider writerProvider;
  private final UserProvider userProvider;
  private final S3Uploader s3Uploader;
  private final SeriesConverter seriesConverter;

  public SeriesAssemble(
    SeriesService seriesService,
    ArticleService articleService,
    ExpulsionService expulsionService,
    WriterProvider writerProvider,
    UserProvider userProvider,
    S3Uploader s3Uploader,
    SeriesConverter seriesConverter
  ) {
    this.seriesService = seriesService;
    this.articleService = articleService;
    this.expulsionService = expulsionService;
    this.writerProvider = writerProvider;
    this.userProvider = userProvider;
    this.s3Uploader = s3Uploader;
    this.seriesConverter = seriesConverter;
  }

  @Transactional
  public SeriesSubscribePost.Response createSeries(
    Long userId,
    MultipartFile thumbnail,
    SeriesSubscribePost.Request request
  ) {
    Writer writer = this.writerProvider.findByUserId(userId);
    Series series = this.seriesConverter.SeriesSubscribePostResponseToEntity(
      writer,
      request
    );
    Long seriesId = this.seriesService.save(series);
    String thumbnailKey = this.uploadThumbnailImage(thumbnail, seriesId);
    series.changeThumbnailKey(thumbnailKey);

    return new SeriesSubscribePost.Response(seriesId);
  }

  public SeriesSubscribeOne.Response getSeriesBySeriesId(Long seriesId) {
    List<Article> articleList = this.articleService.getArticleListBySeriesId(seriesId);
    Series series = this.seriesService.getById(seriesId);

    return this.seriesConverter.seriesToSeriesOneResponse(series, articleList);
  }

  public List<Response> getSeriesList() {
    return this.seriesService.getSeriesList()
      .stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
    Series series = this.seriesService.getById(seriesId);

    return this.seriesConverter.seriesToResponseUsageEdit(series);
  }

  public List<SeriesSubscribeList.Response> getSeriesListOrderBySort(SortType sort) {
    List<Series> seriesList;

    seriesList = switch (sort) {
      case RECENT -> this.seriesService.findSeriesListOrderByCreatedAt();
      case POPULAR -> this.seriesService.findSeriesListOrderByLike();
    };

    return seriesList.stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  @Transactional
  public SeriesSubscribeEdit.Response editSeries(
    Long seriesId,
    SeriesSubscribeEdit.Request request
  ) {
    Series series = this.seriesService.getById(seriesId);
    series.editSeries(request);

    return new SeriesSubscribeEdit.Response(this.seriesService.save(series));
  }

  public List<SeriesSubscribeList.Response> getSeriesSearchTitle(String title) {
    return this.seriesService.getSeriesSearchTitle(title)
      .stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  public List<SeriesSubscribeList.Response> getSeriesSearchNickname(String nickname) {
    return userProvider.findByNickname(nickname)
      .map(user -> {
        Writer writer = this.writerProvider.findByUserId(user.getId());
        return this.seriesService.findAllByWriterId(writer.getId())
          .stream()
          .map(this.seriesConverter::seriesListToResponse)
          .collect(Collectors.toList());
      })
      .orElseGet(Collections::emptyList);
  }

  @Transactional
  public String changeThumbnail(
    MultipartFile thumbnail,
    Long seriesId,
    Long userId
  ) {
    Series series = this.seriesService.getById(seriesId);

    String originalThumbnailKey = series.getThumbnailKey();

    String thumbnailKey = this.uploadThumbnailImage(
      thumbnail,
      seriesId
    );

    expulsionService.save(
      userId, originalThumbnailKey, ExpulsionImageStatus.CREATED,
      ExpulsionImageName.SERIES_THUMBNAIL
    );

    series.changeThumbnailKey(thumbnailKey);

    return this.seriesConverter.toThumbnailEndpoint(thumbnailKey);
  }

  public String uploadThumbnailImage(
    MultipartFile image,
    Long id
  ) {
    String key = Series.class.getSimpleName()
      .toLowerCase()
      + "/" + id.toString()
      + "/thumbnail/"
      + UUID.randomUUID() +
      this.s3Uploader.getExtension(image);

    return this.s3Uploader.upload(Bucket.IMAGE, image, key, S3Uploader.imageExtensions);
  }

}

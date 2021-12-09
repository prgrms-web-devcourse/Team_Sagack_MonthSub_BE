package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.inferface.WriterProvider;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.article.app.ArticleService;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SeriesService {

  private final SeriesRepository seriesRepository;
  private final ArticleService articleService;
  private final WriterProvider writerProvider;
  private final S3Uploader s3Uploader;
  private final SeriesConverter seriesConverter;

  public SeriesService(
    SeriesRepository seriesRepository,
    ArticleService articleService,
    WriterService writerProvider,
    SeriesConverter seriesConverter,
    S3Uploader s3Uploader
  ) {
    this.seriesRepository = seriesRepository;
    this.articleService = articleService;
    this.writerProvider = writerProvider;
    this.seriesConverter = seriesConverter;
    this.s3Uploader = s3Uploader;
  }

  public Series getById(Long id) {
    return this.seriesRepository
      .findById(id)
      .orElseThrow(() -> new SeriesNotFound("id=" + id));
  }

  @Transactional
  public SeriesSubscribePost.Response createSeries(
    Long userId,
    MultipartFile thumbnail,
    SeriesSubscribePost.Request request
  ) throws IOException {
    String imageUrl = this.uploadThumbnailImage(thumbnail, userId);
    Writer writer = this.writerProvider.findByUserId(userId);
    Series entity = this.seriesConverter.SeriesSubscribePostResponseToEntity(
      writer,
      imageUrl,
      request
    );

    return new SeriesSubscribePost.Response(this.seriesRepository.save(entity)
      .getId());
  }

  public SeriesSubscribeOne.Response getSeriesBySeriesId(Long seriesId) {
    List<Article> articleList = this.articleService.getArticleListBySeriesId(seriesId);
    Series series = getById(seriesId);

    return this.seriesConverter.seriesToSeriesOneResponse(series, articleList);
  }

  public List<SeriesSubscribeList.Response> getSeriesList() {
    List<Series> seriesList = this.seriesRepository.findSeriesList();

    return seriesList.stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  public List<SeriesSubscribeList.Response> getSeriesListOrderBySort(SortType sort) {
    List<Series> seriesList;

    seriesList = switch (sort) {
      case RECENT -> this.seriesRepository.findSeriesListOrderByCreatedAt();
      case POPULAR -> this.seriesRepository.findSeriesListOrderByLike();
    };

    return seriesList.stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  @Transactional
  public SeriesSubscribeEdit.Response editSeries(
    Long seriesId,
    Long userId,
    MultipartFile thumbnail,
    SeriesSubscribeEdit.Request request
  ) throws IOException {
    String imageUrl = this.uploadThumbnailImage(thumbnail, userId);
    Series series = this.getById(seriesId);

    series.editSeries(imageUrl, request);

    return new SeriesSubscribeEdit.Response(this.seriesRepository.save(series)
      .getId());
  }

  public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
    Series series = this.getById(seriesId);

    return this.seriesConverter.seriesToResponseUsageEdit(series);
  }

  public String uploadThumbnailImage(
    MultipartFile image,
    Long id
  ) throws IOException {
    String key = Series.class.getSimpleName()
      .toLowerCase()
      + "/" + id.toString()
      + "/thumbnail/"
      + UUID.randomUUID() +
      this.s3Uploader.getExtension(image);

    return this.s3Uploader.upload(Bucket.IMAGE, image, key, S3Uploader.imageExtensions);
  }

  public String updateThumbnailImage(
    MultipartFile image,
    Long id
  ) throws IOException {
    Series series = this.seriesRepository.getById(id);
    String thumbnailKey = this.uploadThumbnailImage(image, series.getId());
    series.changeThumbnailKey(thumbnailKey);

    return this.seriesRepository
      .save(series)
      .getThumbnailKey();
  }

  public boolean checkSeriesStatusByWriterId(
    Long writerId,
    SeriesStatus status
  ) {
    return this.seriesRepository.existsAllByWriterIdAndSubscribeStatus(writerId, status);
  }

  public List<Series> findAllByWriterId(
    Long writerId
  ) {
    return this.seriesRepository.findAllByWriterId(writerId);
  }

}

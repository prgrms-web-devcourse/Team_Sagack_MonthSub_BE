package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
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
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageName;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageStatus;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SeriesService {

  private final SeriesRepository seriesRepository;
  private final ArticleService articleService;
  private final ExpulsionService expulsionService;
  private final WriterProvider writerProvider;
  private final UserProvider userProvider;
  private final S3Uploader s3Uploader;
  private final SeriesConverter seriesConverter;

  public SeriesService(
    SeriesRepository seriesRepository,
    ArticleService articleService,
    ExpulsionService expulsionService,
    WriterService writerProvider,
    UserService userProvider,
    SeriesConverter seriesConverter,
    S3Uploader s3Uploader
  ) {
    this.seriesRepository = seriesRepository;
    this.articleService = articleService;
    this.expulsionService = expulsionService;
    this.writerProvider = writerProvider;
    this.userProvider = userProvider;
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
  ) {
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

  public List<SeriesSubscribeList.Response> getSeriesListSort(SortType sort) {
    return (
      switch (sort) {
        case RECENT -> this.seriesRepository.findAll(Sort.by(Direction.DESC, "id"));
        case POPULAR -> this.seriesRepository.findAll(Sort.by(Direction.DESC, "likes"));
      }).stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  public List<SeriesSubscribeList.Response> getSeriesSearchTitle(String title) {
    return this.seriesRepository.findByTitleContainingIgnoreCase(title)
      .stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList());
  }

  public List<SeriesSubscribeList.Response> getSeriesSearchNickname(String nickname) {
    return userProvider.findByNickname(nickname)
      .map(user -> {
        Writer writer = this.writerProvider.findByUserId(user.getId());
        return this.seriesRepository.findAllByWriterId(writer.getId())
          .stream()
          .map(this.seriesConverter::seriesListToResponse)
          .collect(Collectors.toList());
      })
      .orElseGet(Collections::emptyList);
  }

  @Transactional
  public SeriesSubscribeEdit.Response editSeries(
    Long seriesId,
    SeriesSubscribeEdit.Request request
  ) {
    Series series = this.getById(seriesId);
    series.editSeries(request);

    return new SeriesSubscribeEdit.Response(this.seriesRepository.save(series)
      .getId());
  }

  @Transactional
  public String changeThumbnail(
    MultipartFile thumbnail,
    Long seriesId,
    Long userId
  ) {
    Series series = getById(seriesId);

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

  public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
    Series series = this.getById(seriesId);

    return this.seriesConverter.seriesToResponseUsageEdit(series);
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

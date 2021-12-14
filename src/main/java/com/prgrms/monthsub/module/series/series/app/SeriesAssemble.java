package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.article.app.ArticleService;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.converter.ArticleUploadDateConverter;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribePost;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.DomainType;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileType;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.Status;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
  private final S3Client s3Client;
  private final SeriesConverter seriesConverter;
  private final ArticleUploadDateConverter articleUploadDateConverter;
  private final SeriesUserService seriesUserService;

  public SeriesAssemble(
    SeriesService seriesService,
    ArticleService articleService,
    ExpulsionService expulsionService,
    WriterProvider writerProvider,
    UserProvider userProvider,
    S3Client s3Client,
    SeriesConverter seriesConverter,
    ArticleUploadDateConverter articleUploadDateConverter,
    SeriesUserService seriesUserService
  ) {
    this.seriesService = seriesService;
    this.articleService = articleService;
    this.expulsionService = expulsionService;
    this.writerProvider = writerProvider;
    this.userProvider = userProvider;
    this.s3Client = s3Client;
    this.seriesConverter = seriesConverter;
    this.articleUploadDateConverter = articleUploadDateConverter;
    this.seriesUserService = seriesUserService;
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

    Arrays.stream(request.uploadDate())
      .forEach(uploadDate -> this.seriesService.articleUploadDateSave(
        this.articleUploadDateConverter.ArticleUploadDateRequestToEntity(
          seriesId, uploadDate))
      );

    String thumbnailKey = this.uploadThumbnailImage(thumbnail, seriesId);
    series.changeThumbnailKey(thumbnailKey);

    return new SeriesSubscribePost.Response(seriesId);
  }

  @Transactional
  public SeriesSubscribeEdit.Response editSeries(
    Long seriesId,
    SeriesSubscribeEdit.Request request,
    Optional<MultipartFile> thumbnail,
    Long userId
  ) {
    Series series = this.seriesService.getById(seriesId);
    thumbnail.map(multipartFile -> this.changeThumbnail(multipartFile, series, userId));

    series.editSeries(request);

    return new SeriesSubscribeEdit.Response(this.seriesService.save(series));
  }

  @Transactional
  public String changeThumbnail(
    MultipartFile thumbnail,
    Series series,
    Long userId
  ) {
    if (thumbnail.isEmpty()) {
      return null;
    }
    
    String originalThumbnailKey = series.getThumbnailKey();

    String thumbnailKey = this.uploadThumbnailImage(
      thumbnail,
      series.getId()
    );

    expulsionService.save(
      series.getId(),
      userId,
      originalThumbnailKey,
      Status.CREATED,
      DomainType.SERIES,
      FileCategory.SERIES_THUMBNAIL,
      FileType.IMAGE
    );

    series.changeThumbnailKey(thumbnailKey);

    return thumbnailKey;
  }


  public SeriesSubscribeOne.Response getSeriesBySeriesId(Long seriesId) {
    List<Article> articleList = this.articleService.getArticleListBySeriesId(seriesId);
    Series series = this.seriesService.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesService.getArticleUploadDate(seriesId);

    return this.seriesConverter.seriesToSeriesOneResponse(series, articleList, uploadDateList);
  }

  public SeriesSubscribeList.Response getSeriesListSort(SortType sort) {
    return new SeriesSubscribeList.Response((
      switch (sort) {
        case RECENT -> this.seriesService.findAll(Sort.by(Direction.DESC, "createdAt", "id"));
        case POPULAR -> this.seriesService.findAll(Sort.by(Direction.DESC, "likes"));
      })
      .stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList()));
  }

  public SeriesSubscribeList.Response getSeriesList(
    Long lastSeriesId,
    Integer size
  ) {
    PageRequest cursorPageable = PageRequest.of(
      0,
      size,
      Sort.by(Direction.DESC, "createdAt", "id")
    );

    return new SeriesSubscribeList.Response((
      (lastSeriesId == null) ? this.seriesService.findAll(cursorPageable)
        : this.seriesService.getSeries(lastSeriesId, cursorPageable)
    )
      .stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList()));
  }

  public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
    Series series = this.seriesService.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesService.getArticleUploadDate(seriesId);

    return this.seriesConverter.seriesToResponseUsageEdit(series, uploadDateList);
  }

  public SeriesSubscribeList.Response getSeriesSearchTitle(String title) {
    return new SeriesSubscribeList.Response(this.seriesService.getSeriesSearchTitle(title)
      .stream()
      .map(this.seriesConverter::seriesListToResponse)
      .collect(Collectors.toList()));
  }

  public SeriesSubscribeList.Response getSeriesSearchNickname(String nickname) {
    return this.userProvider.findByNickname(nickname)
      .map(user -> {
        Writer writer = this.writerProvider.findByUserId(user.getId());
        return new SeriesSubscribeList.Response(this.seriesService.findAllByWriterId(writer.getId())
          .stream()
          .map(this.seriesConverter::seriesListToResponse)
          .collect(Collectors.toList()));
      })
      .orElseGet(() -> {return new SeriesSubscribeList.Response(Collections.emptyList());});
  }

  public SeriesSubscribeList.Response getSeriesSubscribeList(Long userId) {
    return new Response(this.seriesUserService.findAllMySubscribeByUserId(userId)
      .stream()
      .map(seriesUser -> seriesConverter.seriesListToResponse(seriesUser.getSeries()))
      .collect(Collectors.toList()));
  }

  public SeriesSubscribeList.Response getSeriesPostList(Long userId) {
    return new SeriesSubscribeList.Response(
      this.seriesService.findAllByWriterId(this.writerProvider.findWriterByUserId(userId)
          .getId())
        .stream()
        .map(seriesConverter::seriesListToResponse)
        .collect(Collectors.toList())
    );
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
      this.s3Client.getExtension(image);

    return this.s3Client.upload(Bucket.IMAGE, image, key);
  }

}

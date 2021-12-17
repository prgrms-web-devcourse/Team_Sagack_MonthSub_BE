package com.prgrms.monthsub.module.series.series.app;

import static java.util.Optional.ofNullable;

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
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.type.SortType;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
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
import org.springframework.security.access.AccessDeniedException;
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
  private final SeriesLikesService seriesLikesService;

  public SeriesAssemble(
    SeriesService seriesService,
    ArticleService articleService,
    ExpulsionService expulsionService,
    WriterProvider writerProvider,
    UserProvider userProvider,
    S3Client s3Client,
    SeriesConverter seriesConverter,
    ArticleUploadDateConverter articleUploadDateConverter,
    SeriesUserService seriesUserService,
    SeriesLikesService seriesLikesService
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
    this.seriesLikesService = seriesLikesService;
  }

  @Transactional
  public SeriesSubscribePost.Response createSeries(
    Long userId,
    MultipartFile thumbnail,
    SeriesSubscribePost.Request request
  ) {
    Writer writer = this.writerProvider.findByUserId(userId);
    Series series = this.seriesConverter.toEntity(writer, request);
    Long seriesId = this.seriesService.save(series);

    Arrays.stream(request.uploadDate())
      .forEach(uploadDate -> {
          ArticleUploadDate articleUploadDate = this.articleUploadDateConverter.toEntity(
            seriesId, uploadDate
          );

          this.seriesService.articleUploadDateSave(articleUploadDate);
        }
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

    if (!series.isMine(userId)) {
      throw new AccessDeniedException("수정 권한이 없습니다.");
    }

    return new SeriesSubscribeEdit.Response(this.seriesService.save(series), true);
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


  public SeriesSubscribeOne.Response getSeriesBySeriesId(
    Long seriesId,
    Optional<Long> userId
  ) {
    List<Article> articleList = this.articleService.getArticleListBySeriesId(seriesId);
    Series series = this.seriesService.getById(seriesId);

    List<ArticleUploadDate> uploadDateList = this.seriesService.getArticleUploadDate(seriesId);

    return ofNullable(userId).map(user ->
        this.seriesConverter.toSeriesOne(
          series, articleList, uploadDateList, series.isMine(user.get())))
      .orElse(this.seriesConverter.toSeriesOne(series, articleList, uploadDateList, false));
  }

  public SeriesSubscribeList.Response getSeriesListSort(SortType sort) {
    return new SeriesSubscribeList.Response((
      switch (sort) {
        case RECENT -> this.seriesService.findAll(Sort.by(Direction.DESC, "createdAt", "id"));
        case POPULAR -> this.seriesService.findAll(Sort.by(Direction.DESC, "likes"));
      })
      .stream()
      .map(this.seriesConverter::toResponse)
      .collect(Collectors.toList()));
  }

  public SeriesSubscribeList.Response getSeriesList(
    Long lastSeriesId,
    Integer size,
    List<Category> categories
  ) {
    PageRequest cursorPageable = PageRequest.of(
      0,
      size,
      Sort.by(Direction.DESC, "createdAt", "id")
    );

    return new SeriesSubscribeList.Response((
      (lastSeriesId == null) ?
        categories.contains(Category.ALL) ? this.seriesService.findAll(cursorPageable)
          : this.seriesService.getSeriesByCategories(categories, cursorPageable)
        : categories.contains(Category.ALL) ?
          this.seriesService.getSeries(lastSeriesId, cursorPageable)
          : this.seriesService.getSeriesByCategoriesLessThanId(
            lastSeriesId, categories, cursorPageable))
      .stream()
      .map(this.seriesConverter::toResponse)
      .collect(Collectors.toList())
    );
  }

  private List<Series> getSeries(
    Long lastSeriesId,
    PageRequest cursorPageable
  ) {
    return Optional.ofNullable(lastSeriesId)
      .map(lastId -> this.seriesService.getSeries(lastId, cursorPageable))
      .orElse(this.seriesService.findAll(cursorPageable));
  }

  public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
    Series series = this.seriesService.getById(seriesId);
    List<ArticleUploadDate> uploadDateList = this.seriesService.getArticleUploadDate(seriesId);

    return this.seriesConverter.toResponseUsageEdit(series, uploadDateList);
  }

  public SeriesSubscribeList.Response getSeriesSearchTitle(String title) {
    return new SeriesSubscribeList.Response(
      this.seriesService
        .getSeriesSearchTitle(title)
        .stream()
        .map(this.seriesConverter::toResponse)
        .collect(Collectors.toList())
    );
  }

  public SeriesSubscribeList.Response getSeriesSearchNickname(String nickname) {
    return this.userProvider.findByNickname(nickname)
      .map(user -> {
        Writer writer = this.writerProvider.findByUserId(user.getId());

        return new SeriesSubscribeList.Response(
          this.seriesService
            .findAllByWriterId(writer.getId())
            .stream()
            .map(this.seriesConverter::toResponse)
            .collect(Collectors.toList())
        );
      })
      .orElseGet(() -> new SeriesSubscribeList.Response(Collections.emptyList()));
  }

  public SeriesSubscribeList.Response getSeriesSubscribeList(Long userId) {
    List<Long> likeSeriesList = this.seriesLikesService.findAllByUserId(userId);

    return new SeriesSubscribeList.Response(
      this.seriesUserService
        .findAllMySubscribeByUserId(userId)
        .stream()
        .map(seriesUser -> {
          Series series = seriesUser.getSeries();
          if (likeSeriesList.contains(series.getId())) {
            series.changeSeriesIsLiked(true);
          }
          return series;
        })
        .map(seriesConverter::toResponse)
        .collect(Collectors.toList())
    );
  }

  public SeriesSubscribeList.Response getSeriesPostList(Long userId) {
    List<Long> likeSeriesList = this.seriesLikesService.findAllByUserId(userId);

    return new SeriesSubscribeList.Response(
      this.seriesService
        .findAllByWriterId(this.writerProvider.findByUserId(userId).getId())
        .stream()
        .map(series -> {
          if (likeSeriesList.contains(series.getId())) {
            series.changeSeriesIsLiked(true);
          }
          return series;
        })
        .map(seriesConverter::toResponse)
        .collect(Collectors.toList())
    );
  }

  public String uploadThumbnailImage(
    MultipartFile image,
    Long id
  ) {
    String key = "series/" + id.toString()
      + "/thumbnail/"
      + UUID.randomUUID() +
      this.s3Client.getExtension(image);

    return this.s3Client.upload(Bucket.IMAGE, image, key);
  }

}

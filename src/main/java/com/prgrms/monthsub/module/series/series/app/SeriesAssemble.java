package com.prgrms.monthsub.module.series.series.app;

import static com.prgrms.monthsub.module.series.series.domain.Series.Category.getCategories;
import static com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus.getAllStatus;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.payment.bill.app.provider.PaymentProvider;
import com.prgrms.monthsub.module.series.article.app.ArticleService;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.converter.ArticleUploadDateConverter;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
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
import java.time.LocalDateTime;
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

  private final int PAGE_NUM = 0;
  private final int POPULAR_SERIES_SIZE = 10;
  private final SeriesService seriesService;
  private final ArticleService articleService;
  private final ExpulsionService expulsionService;
  private final WriterProvider writerProvider;
  private final UserProvider userProvider;
  private final S3Client s3Client;
  private final SeriesConverter seriesConverter;
  private final ArticleUploadDateConverter articleUploadDateConverter;
  private final SeriesLikesService seriesLikesService;
  private final PaymentProvider paymentProvider;
  private List<Category> categoryList;

  public SeriesAssemble(
    SeriesService seriesService,
    ArticleService articleService,
    ExpulsionService expulsionService,
    WriterProvider writerProvider,
    UserProvider userProvider,
    S3Client s3Client,
    SeriesConverter seriesConverter,
    ArticleUploadDateConverter articleUploadDateConverter,
    SeriesLikesService seriesLikesService,
    PaymentProvider paymentProvider
  ) {
    this.seriesService = seriesService;
    this.articleService = articleService;
    this.expulsionService = expulsionService;
    this.writerProvider = writerProvider;
    this.userProvider = userProvider;
    this.s3Client = s3Client;
    this.seriesConverter = seriesConverter;
    this.articleUploadDateConverter = articleUploadDateConverter;
    this.seriesLikesService = seriesLikesService;
    this.paymentProvider = paymentProvider;
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
    if (!series.isMine(userId)) {
      throw new AccessDeniedException("수정 권한이 없습니다.");
    }

    thumbnail.map(multipartFile -> this.changeThumbnail(multipartFile, series, userId));

    series.editSeries(request);

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

  public SeriesSubscribeList.Response getPopularSeriesList() {
    List<Series> popularSeriesList = this.seriesService.findAll(PageRequest.of(
      PAGE_NUM,
      POPULAR_SERIES_SIZE,
      Sort.by(Direction.DESC, "likes")
    ));

    return new Response(popularSeriesList.stream()
      .map(seriesConverter::toResponse)
      .collect(Collectors.toList())
    );
  }

  public SeriesSubscribeList.Response getRecentSeriesList(Optional<Long> userIdOrEmpty) {
    List<Long> likeSeriesList = userIdOrEmpty.map(
        this.seriesLikesService::findAllByUserId
      )
      .orElse(Collections.emptyList());

    List<Series> recentSeriesList = this.seriesService.findBySubscribeStatus(
        SeriesStatus.SUBSCRIPTION_AVAILABLE,
        PageRequest.of(PAGE_NUM, POPULAR_SERIES_SIZE, Sort.by(Direction.DESC, "createdAt", "id"))
      )
      .stream()
      .peek(series -> {
        if (likeSeriesList.contains(series.getId())) {
          series.changeSeriesIsLiked(true);
        }
      })
      .collect(Collectors.toList());

    return new Response(recentSeriesList.stream()
      .map(seriesConverter::toResponse).collect(
        Collectors.toList())
    );
  }

  public SeriesSubscribeOne.Response getSeriesOne(
    Long id,
    Optional<Long> userIdOrEmpty
  ) {
    List<Long> likeSeriesList = userIdOrEmpty.map(
        this.seriesLikesService::findAllByUserId
      )
      .orElse(Collections.emptyList());

    List<Article> articleList = this.articleService.getArticleListBySeriesId(id);
    Series series = this.seriesService.getById(id);
    if (likeSeriesList.contains(series.getId())) {
      series.changeSeriesIsLiked(true);
    }
    List<ArticleUploadDate> uploadDateList = this.seriesService.getArticleUploadDate(id);

    return userIdOrEmpty
      .map(userId ->
        this.seriesConverter.toSeriesOne(
          series, articleList, uploadDateList, series.isMine(userId)))
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

  public SeriesSubscribeOne.UsageEditResponse getSeriesUsageEdit(Long seriesId) {
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
      this.paymentProvider
        .findAllMySubscribeByUserId(userId)
        .stream()
        .map(payment -> {
          Series series = payment.getSeries();
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
    //TODO user table part id로 part 확인해야함.
    return writerProvider.findByUserIdOrEmpty(userId).map(writer -> {
          List<Long> likeSeriesList = this.seriesLikesService.findAllByUserId(userId);
          return new SeriesSubscribeList.Response(
            this.seriesService
              .findAllByWriterId(this.writerProvider.findByUserId(userId).getId())
              .stream()
              .peek(series -> {
                if (likeSeriesList.contains(series.getId())) {
                  series.changeSeriesIsLiked(true);
                }
              })
              .map(seriesConverter::toResponse)
              .collect(Collectors.toList())
          );
        }
      )
      .orElse(
        new SeriesSubscribeList.Response(Collections.emptyList())
      );
  }

  public SeriesSubscribeList.Response getSeriesList(
    Optional<Long> lastSeriesId,
    Integer size,
    List<Category> categories,
    Optional<Long> userIdOrEmpty,
    List<SeriesStatus> status
  ) {
    categoryList = categories.isEmpty() ? getCategories() : categories;
    List<SeriesStatus> finalStatus = status.isEmpty() ? getAllStatus() : status;
    List<Long> likeSeriesList = userIdOrEmpty.map(
        this.seriesLikesService::findAllByUserId
      )
      .orElse(Collections.emptyList());

    return new SeriesSubscribeList.Response(
      lastSeriesId.map(id -> {
          LocalDateTime createdAt = this.seriesService.getById(id).getCreatedAt();
          return this.seriesService.findAllByCategory(
            id,
            size,
            categoryList,
            createdAt
          );
        }).orElse(
          this.seriesService.findAllByCategoryIn(
            categoryList, PageRequest.of(
              PAGE_NUM,
              size,
              Sort.by(Direction.DESC, "createdAt", "id")
            )
          ))
        .stream()
        .peek(series -> {
          if (likeSeriesList.contains(series.getId())) {
            series.changeSeriesIsLiked(true);
          }
        })
        .filter(series -> finalStatus.contains(series.getSubscribeStatus()))
        .map(this.seriesConverter::toResponse)
        .collect(Collectors.toList()));
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

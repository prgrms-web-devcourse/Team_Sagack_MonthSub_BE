package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.payment.bill.app.provider.PaymentProvider;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.domain.exception.ArticleException.ViewUnAuthorize;
import com.prgrms.monthsub.module.series.article.dto.ArticleEdit;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.app.SeriesService;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.worker.expulsion.app.provider.ExpulsionProvider;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.DomainType;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.FileType;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.Status;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ArticleAssemble {

  private final ArticleService articleService;
  private final SeriesService seriesService;
  private final ExpulsionProvider expulsionProvider;
  private final ArticleConverter articleConverter;
  private final S3Client s3Client;
  private final PaymentProvider paymentProvider;

  public ArticleAssemble(
    ArticleService articleService,
    SeriesService seriesService,
    ExpulsionProvider expulsionProvider,
    ArticleConverter articleConverter,
    S3Client s3Client,
    PaymentProvider paymentProvider
  ) {
    this.articleService = articleService;
    this.seriesService = seriesService;
    this.expulsionProvider = expulsionProvider;
    this.articleConverter = articleConverter;
    this.s3Client = s3Client;
    this.paymentProvider = paymentProvider;
  }

  @Transactional
  public ArticlePost.Response createArticle(
    MultipartFile thumbnail,
    ArticlePost.Request request,
    Long userId
  ) {
    Series series = this.seriesService.getById(request.seriesId());
    Long articleCount = this.articleService.countBySeriesId(request.seriesId());
    Article article = this.articleConverter.toEntity(series, request, articleCount.intValue() + 1);

    if (!article.isMine(userId)) {
      final String message = "articleId=" + article.getId() + ", userId=" + userId;
      throw new AccessDeniedException(message + ":생성 권한이 없습니다.");
    }

    this.articleService.save(article);

    String thumbnailKey = this.uploadThumbnailImage(
      thumbnail,
      request.seriesId(),
      article.getId()
    );

    article.changeThumbnailKey(thumbnailKey);

    return new ArticlePost.Response(article.getId(), true);
  }

  @Transactional
  public ArticleEdit.ChangeResponse editArticle(
    Long id,
    ArticleEdit.ChangeRequest request,
    Optional<MultipartFile> thumbnail,
    Long userId
  ) {
    Article article = articleService.find(id);

    if (!article.isMine(userId)) {
      final String message = "articleId=" + article.getId() + ", userId=" + userId;
      throw new AccessDeniedException(message + ":수정 권한이 없습니다.");
    }

    thumbnail.map(
      multipartFile -> this.changeThumbnail(multipartFile, request.seriesId(), article, userId)
    );
    article.changeWriting(request.title(), request.contents());

    return new ArticleEdit.ChangeResponse(article.getId(), true);
  }

  public ArticleOne.Response getArticle(
    Long id,
    Long seriesId,
    Long userId
  ) {
    Article article = articleService.find(id);

    if (!article.isMine(userId)) {
      final String message = "articleId=" + article.getId() + ", userId=" + userId;

      this.paymentProvider.find(userId, seriesId)
        .orElseThrow(() -> new ViewUnAuthorize(message + ":결제 후 이용해주세요."));
    }

    Long articleCount = this.articleService.countBySeriesId(seriesId);

    return articleConverter.toArticleOneResponse(
      article.isMine(userId), article, articleCount, article.getSeries()
        .getWriter()
        .getUser()
    );
  }

  @Transactional
  public void deleteArticle(
    Long id,
    Long seriesId,
    Long userId
  ) {
    Article article = articleService.find(id);

    if (!article.isMine(userId)) {
      final String message = "articleId=" + article.getId() + ", userId=" + userId;
      throw new AccessDeniedException(message + ":삭제 권한이 없습니다.");
    }

    // 1. 아티클 삭제
    this.articleService.deleteById(id);

    // 2. 아티클 round 업데이트하기
    AtomicInteger index = new AtomicInteger(1);
    this.articleService.getArticleListBySeriesId(seriesId)
      .forEach(a -> {
        a.changeRound(index.getAndIncrement());
      });
  }

  @Transactional
  public String changeThumbnail(
    MultipartFile thumbnail,
    Long seriesId,
    Article article,
    Long userId
  ) {
    if (thumbnail.isEmpty()) {
      return null;
    }

    String originalThumbnailKey = article.getThumbnailKey();
    String thumbnailKey = this.uploadThumbnailImage(
      thumbnail,
      seriesId,
      article.getId()
    );

    expulsionProvider.save(
      article.getId(),
      userId,
      originalThumbnailKey,
      Status.CREATED,
      DomainType.ARTICLE,
      FileCategory.ARTICLE_THUMBNAIL,
      FileType.IMAGE
    );

    article.changeThumbnailKey(thumbnailKey);

    return thumbnailKey;
  }

  protected String uploadThumbnailImage(
    MultipartFile image,
    Long seriesId,
    Long articleId
  ) {
    String key = "series/" + seriesId.toString()
      + "/article/" + articleId.toString()
      + "/thumbnail/"
      + UUID.randomUUID() +
      this.s3Client.getExtension(image);

    return this.s3Client.upload(Bucket.IMAGE, image, key);
  }

}

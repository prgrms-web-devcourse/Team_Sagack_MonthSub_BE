package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.domain.exception.ArticleException.ArticleNotCreate;
import com.prgrms.monthsub.module.series.article.domain.exception.ArticleException.ArticleNotUpdate;
import com.prgrms.monthsub.module.series.article.dto.ArticleEdit;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.app.SeriesService;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.DomainType;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileType;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.Status;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ArticleAssemble {

  private final ArticleService articleService;
  private final SeriesService seriesService;
  private final ExpulsionService expulsionService;
  private final UserProvider userProvider;
  private final ArticleConverter articleConverter;
  private final S3Client s3Client;

  public ArticleAssemble(
    ArticleService articleService,
    SeriesService seriesService,
    ExpulsionService expulsionService,
    UserProvider userProvider,
    ArticleConverter articleConverter,
    S3Client s3Client
  ) {
    this.articleService = articleService;
    this.seriesService = seriesService;
    this.expulsionService = expulsionService;
    this.userProvider = userProvider;
    this.articleConverter = articleConverter;
    this.s3Client = s3Client;
  }

  @Transactional
  public ArticlePost.Response createArticle(
    MultipartFile thumbnail,
    ArticlePost.Request request,
    Long userId
  ) {
    Series series = this.seriesService.getById(request.seriesId());

    Long articleCount = this.articleService.countBySeriesId(request.seriesId());

    Article article = this.articleConverter.ArticlePostToEntity(
      series,
      request,
      articleCount.intValue() + 1
    );

    boolean isMine = Objects.equals(article.getSeries()
      .getWriter()
      .getUser()
      .getId(), userId);

    if (!isMine) {
      throw new ArticleNotCreate();
    }

    this.articleService.save(article);

    String thumbnailKey = this.uploadThumbnailImage(
      thumbnail,
      request.seriesId(),
      article.getId()
    );

    article.changeThumbnailKey(thumbnailKey);

    return new ArticlePost.Response(article.getId(), isMine);
  }

  @Transactional
  public ArticleEdit.ChangeResponse editArticle(
    Long id,
    ArticleEdit.ChangeRequest request,
    Optional<MultipartFile> thumbnail,
    Long userId
  ) {
    Article article = articleService.find(id);

    boolean isMine = Objects.equals(article.getSeries()
      .getWriter()
      .getUser()
      .getId(), userId);

    if (!isMine) {
      throw new ArticleNotUpdate();
    }

    thumbnail.map(
      multipartFile -> this.changeThumbnail(multipartFile, request.seriesId(), article, userId));
    article.changeWriting(request.title(), request.contents());

    return new ArticleEdit.ChangeResponse(article.getId(), isMine);
  }

  public ArticleOne.Response getArticleOne(
    Long id,
    Long seriesId,
    Long userId
  ) {
    Article article = articleService.find(id);
    Long articleCount = this.articleService.countBySeriesId(seriesId);
    User user = userProvider.findById(userId);

    return articleConverter.articleToArticleOneResponse(article, articleCount, user);
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

    expulsionService.save(
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
    String key = Series.class.getSimpleName()
      .toLowerCase()
      + "/" + seriesId.toString()
      + "/" + Article.class.getSimpleName()
      .toLowerCase()
      + "/" + articleId.toString()
      + "/thumbnail/"
      + UUID.randomUUID() +
      this.s3Client.getExtension(image);

    return this.s3Client.upload(Bucket.IMAGE, image, key);
  }

}

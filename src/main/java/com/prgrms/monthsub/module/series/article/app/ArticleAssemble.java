package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.app.SeriesService;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.io.IOException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ArticleAssemble {

  private final ArticleService articleService;

  private final SeriesService seriesService;

  private final ArticleConverter articleConverter;

  public ArticleAssemble(
    ArticleService articleService,
    SeriesService seriesService,
    ArticleConverter articleConverter
  ) {
    this.articleService = articleService;
    this.seriesService = seriesService;
    this.articleConverter = articleConverter;
  }

  @Transactional
  public ArticlePost.Response createArticle(
    MultipartFile thumbnail,
    ArticlePost.Request request
  ) throws IOException {
    Series series = this.seriesService.getById(request.seriesId());

    Long articleCount = this.articleService.countBySeriesId(request.seriesId());

    Article article = articleConverter.ArticlePostToEntity(
      series,
      request,
      articleCount.intValue() + 1
    );

    String thumbnailKey = this.articleService.uploadThumbnailImage(
      thumbnail,
      request.seriesId(),
      article.getId()
    );

    article.changeThumbnailKey(thumbnailKey);

    return new ArticlePost.Response(article.getId());
  }

}

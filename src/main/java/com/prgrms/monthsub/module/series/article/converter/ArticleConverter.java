package com.prgrms.monthsub.module.series.article.converter;

import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.BriefArticleBySeriesIdResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {

  public Article ArticlePostToEntity(
    Series series,
    ArticlePost.Request request,
    int round
  ) {
    return Article.builder()
      .title(request.title())
      .contents(request.contents())
      .round(round)
      .series(series)
      .build();
  }

  public BriefArticleBySeriesIdResponse articleToArticleBySeriesIdResponse(Article article) {
    return new BriefArticleBySeriesIdResponse(
      article.getId(),
      article.getTitle(),
      article.getRound(),
      article.getCreatedAt()
        .toLocalDate()
    );
  }

}

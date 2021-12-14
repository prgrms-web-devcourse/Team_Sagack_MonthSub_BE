package com.prgrms.monthsub.module.series.article.converter;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.BriefArticleBySeriesIdResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {

  private final S3 s3;

  public ArticleConverter(S3 s3) {this.s3 = s3;}

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

  public ArticleOne.Response articleToArticleOneResponse(
    Article article,
    Long articleCount,
    User user
  ) {
    return new ArticleOne.Response(
      article.getTitle(),
      article.getContents(),
      this.toThumbnailEndpoint(article.getThumbnailKey()),
      articleCount.intValue(),
      article.getCreatedAt()
        .toLocalDate()
        .toString(),
      user.getNickname(),
      (user.getProfileKey() == null ? null : this.s3.getDomain() + "/" + user.getProfileKey()),
      user.getProfileIntroduce()
    );
  }

  public String toThumbnailEndpoint(String thumbnailKey) {
    return this.s3.getDomain() + "/" + thumbnailKey;
  }

}

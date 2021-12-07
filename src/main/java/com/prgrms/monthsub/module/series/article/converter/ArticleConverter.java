package com.prgrms.monthsub.module.series.article.converter;

import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.BriefArticleBySeriesIdResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {

    public BriefArticleBySeriesIdResponse articleToArticleBySeriesIdResponse(Article article) {
        return new BriefArticleBySeriesIdResponse(
            article.getId(),
            article.getTitle(),
            article.getRound(),
            article.getCreatedAt().toLocalDate()
        );
    }

}

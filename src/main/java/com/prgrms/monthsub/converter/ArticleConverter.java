package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.Article;
import com.prgrms.monthsub.dto.SeriesSubscribeList.BriefArticleBySeriesIdResponse;
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

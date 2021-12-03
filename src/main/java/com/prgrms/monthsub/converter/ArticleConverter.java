package com.prgrms.monthsub.converter;

import com.prgrms.monthsub.domain.Article;
import com.prgrms.monthsub.dto.response.ArticleGetListBySeriesIdResponse;
import org.springframework.stereotype.Component;

@Component
public class ArticleConverter {

    public ArticleGetListBySeriesIdResponse articleToArticleGetListBySeriesIdResponse(Article article) {
        return new ArticleGetListBySeriesIdResponse(
            article.getId(),
            article.getTitle(),
            article.getContents(),
            article.getRound()
        );
    }

}

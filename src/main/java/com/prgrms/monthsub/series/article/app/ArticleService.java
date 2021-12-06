package com.prgrms.monthsub.series.article.app;

import com.prgrms.monthsub.series.article.app.ArticleRepository;
import com.prgrms.monthsub.series.article.domain.Article;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> getArticleListBySeriesId(Long seriesId) {
        return articleRepository.findAllArticleBySeriesId(seriesId);
    }

}

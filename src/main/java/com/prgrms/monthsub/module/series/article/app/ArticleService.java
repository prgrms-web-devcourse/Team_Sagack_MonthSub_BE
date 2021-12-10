package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.domain.exception.ArticleException.ArticleNotFound;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ArticleService {

  private final ArticleRepository articleRepository;

  public ArticleService(
    ArticleRepository articleRepository
  ) {
    this.articleRepository = articleRepository;
  }

  @Transactional
  public Long save(Article article) {
    return this.articleRepository.save(article)
      .getId();
  }

  public Article find(Long id) {
    return this.articleRepository.findById(id)
      .orElseThrow(() -> new ArticleNotFound("articleId=" + id));
  }

  public List<Article> getArticleListBySeriesId(Long seriesId) {
    return this.articleRepository.findAllBySeriesId(seriesId);
  }

  public Long countBySeriesId(Long seriesId) {
    return this.articleRepository.countBySeriesId(seriesId);
  }

}

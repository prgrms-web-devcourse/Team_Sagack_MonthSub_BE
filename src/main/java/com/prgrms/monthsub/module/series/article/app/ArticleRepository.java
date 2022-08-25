package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.module.series.article.domain.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  List<Article> findAllBySeriesId(Long seriesId);

  long countBySeriesId(Long seriesId);

  void deleteAllBySeriesId(Long seriesId);

}

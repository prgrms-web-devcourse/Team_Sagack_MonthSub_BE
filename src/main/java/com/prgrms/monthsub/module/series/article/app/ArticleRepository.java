package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.module.series.article.domain.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

  @Query("select a from Article as a where a.series.id = :seriesId")
  List<Article> findAllArticleBySeriesId(@Param("seriesId") Long seriesId);

  long countBySeriesId(Long seriesId);

}

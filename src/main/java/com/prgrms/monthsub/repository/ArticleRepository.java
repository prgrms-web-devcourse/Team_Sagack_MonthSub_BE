package com.prgrms.monthsub.repository;

import com.prgrms.monthsub.domain.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("select a from Article as a where a.series = :seriesId")
    List<Article> findAllArticleBySeriesId(@Param("seriesId") Long seriesId);

}
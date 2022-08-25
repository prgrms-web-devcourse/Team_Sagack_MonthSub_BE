package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleUploadDateRepository extends JpaRepository<ArticleUploadDate, Long> {

  List<ArticleUploadDate> findAllBySeriesId(Long seriesId);

  void deleteAllBySeriesId(Long seriesId);

}

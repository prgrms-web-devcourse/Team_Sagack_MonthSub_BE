package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomSeriesRepository extends JpaRepository<Series, Long>,
  DynamicSeriesRepository {

  boolean existsAllByWriterIdAndSubscribeStatus(
    Long writerId,
    SeriesStatus subscribeStatus
  );

  List<Series> findByTitleContainingIgnoreCase(String title);

  List<Series> findAllByWriterId(Long writerId);

  List<Series> findAllByCategoryIn(
    List<Category> categories,
    Pageable pageable
  );

  List<Series> findBySubscribeStatus(
    SeriesStatus subscribeStatus,
    Pageable pageable
  );

}

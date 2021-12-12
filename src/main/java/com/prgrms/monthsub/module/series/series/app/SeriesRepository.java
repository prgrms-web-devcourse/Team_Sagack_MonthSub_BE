package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {

  boolean existsAllByWriterIdAndSubscribeStatus(
    Long writerId,
    SeriesStatus subscribeStatus
  );

  List<Series> findByTitleContainingIgnoreCase(String title);

  List<Series> findAllByWriterId(Long writerId);

  List<Series> findByIdLessThan(
    Long id,
    Pageable pageable
  );

}

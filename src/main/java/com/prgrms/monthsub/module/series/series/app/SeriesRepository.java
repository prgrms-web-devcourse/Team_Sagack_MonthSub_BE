package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeriesRepository extends JpaRepository<Series, Long> {

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu")
  List<Series> findSeriesList();

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu order by s.likes desc")
  List<Series> findSeriesListOrderByLike();

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu order by s.createdAt desc")
  List<Series> findSeriesListOrderByCreatedAt();

  boolean existsAllByWriterIdAndSubscribeStatus(
    Long writerId,
    SeriesStatus subscribeStatus
  );

  List<Series> findByTitleContainingIgnoreCase(String title);

  List<Series> findAllByWriterId(Long writerId);

}

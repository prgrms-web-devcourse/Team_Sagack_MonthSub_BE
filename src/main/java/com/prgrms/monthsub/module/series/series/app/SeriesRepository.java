package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

  @Query("select s from Series as s join fetch s.writer where s.writer.id = :writerId")
  List<Series> findAllByWriterId(@Param("writerId") Long writerId);

}

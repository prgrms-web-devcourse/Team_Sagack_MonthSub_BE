package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesRepository extends JpaRepository<Series, Long> {

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu")
  List<Series> findSeriesList();

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu order by s.likes desc")
  List<Series> findSeriesListOrderByLike();

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu order by s.id desc")
  List<Series> findSeriesListOrderById();

  @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu where s.id = :seriesId and sw.id = :writerId")
  Optional<Series> findSeriesByIdAndWriterId(
    @Param("seriesId") Long seriesId,
    @Param("writerId") Long writerId
  );

  @Query("select s.subscribeStatus from Series as s where s.writer.id = :writerId and s.subscribeStatus = :seriesStatus")
  Page<SeriesStatus> checkSeriesStatusByWriterId(
    @Param("writerId") Long writerId,
    @Param("seriesStatus") SeriesStatus seriesStatus,
    Pageable pageable
  );

  @Query("select s from Series as s join fetch s.writer where s.writer.id = :writerId")
  List<Series> findAllByWriterId(@Param("writerId") Long writerId);

}

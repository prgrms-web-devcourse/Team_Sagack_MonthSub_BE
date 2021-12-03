package com.prgrms.monthsub.repository;

import com.prgrms.monthsub.domain.Series;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("select s from Series as s join fetch s.writer sw join fetch sw.user swu where s.id = :seriesId")
    Optional<Series> findSeriesById(@Param("seriesId") Long seriesId);

}
package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesLikes;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesLikesRepository extends JpaRepository<SeriesLikes, Long> {

    @Query("select sl from SeriesLikes as sl join fetch sl.series se where sl.userId = :userId and se.id = :seriesId")
    Optional<SeriesLikes> findSeriesLikesByUserIdAndSeriesId(@Param("userId") Long userId,
        @Param("seriesId") Long seriesId);

}

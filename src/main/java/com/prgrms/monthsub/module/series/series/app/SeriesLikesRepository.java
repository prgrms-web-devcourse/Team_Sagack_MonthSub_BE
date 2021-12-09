package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesLikes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesLikesRepository extends JpaRepository<SeriesLikes, Long> {

  Optional<SeriesLikes> findSeriesLikesByUserIdAndSeriesId(
    Long userId,
    Long seriesId
  );

  List<SeriesLikes> findAllByUserId(Long userId);

}

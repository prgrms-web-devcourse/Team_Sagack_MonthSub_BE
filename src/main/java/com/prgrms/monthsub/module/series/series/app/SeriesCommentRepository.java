package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesCommentRepository extends JpaRepository<SeriesComment, Long> {

  List<SeriesComment> findAllBySeriesIdAndParentIdIsNull(Long seriesId);

  List<SeriesComment> findAllBySeriesIdAndParentIdIsNotNull(Long seriesId);

}

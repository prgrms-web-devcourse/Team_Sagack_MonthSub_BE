package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomSeriesCommentRepository extends JpaRepository<SeriesComment, Long>,
  DynamicSeriesCommentRepository {

  List<SeriesComment> findAllBySeriesIdAndParentIdIsNull(Long seriesId, Pageable pageable);

  List<SeriesComment> findAllBySeriesIdAndParentIdIsNotNull(Long seriesId);

}

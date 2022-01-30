package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import java.time.LocalDateTime;
import java.util.List;

public interface DynamicSeriesCommentRepository {

  List<SeriesComment> findAll(
    Long seriesId,
    Long lastId,
    int size,
    LocalDateTime createdAt
  );

}

package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import java.time.LocalDateTime;
import java.util.List;

public interface DynamicSeriesRepository {

  List<Series> findAllByCategory(
    Long lastSeriesId,
    int size,
    List<Category> categories,
    LocalDateTime createdAt
  );

}

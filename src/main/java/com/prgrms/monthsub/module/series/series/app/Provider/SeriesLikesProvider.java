package com.prgrms.monthsub.module.series.series.app.Provider;

import java.util.List;

public interface SeriesLikesProvider {

  List<Long> findAllByUserId(Long userId);

}

package com.prgrms.monthsub.module.series.series.app.Provider;

import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import java.util.List;

public interface SeriesProvider {

  Series getById(Long id);

  List<ArticleUploadDate> getArticleUploadDate(Long seriesId);

}

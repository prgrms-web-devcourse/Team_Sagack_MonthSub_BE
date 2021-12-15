package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate.UploadDate;
import org.springframework.stereotype.Component;

@Component
public class ArticleUploadDateConverter {

  public ArticleUploadDateConverter() {
  }

  public ArticleUploadDate toEntity(
    Long seriesId,
    String uploadDate
  ) {
    return ArticleUploadDate.builder()
      .seriesId(seriesId)
      .uploadDate(UploadDate.of(uploadDate))
      .build();
  }

}

package com.prgrms.monthsub.module.series.article.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class ArticleException {

  public static class ArticleNotFound extends BusinessException {
    public ArticleNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message)
        .toList()
        .toString()));
    }
  }

}

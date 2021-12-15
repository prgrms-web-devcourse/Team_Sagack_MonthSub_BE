package com.prgrms.monthsub.module.series.article.domain.exception;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.base.InvalidInputException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;
import java.util.Arrays;

public class ArticleException {

  public static class ArticleNotFound extends BusinessException {
    public ArticleNotFound(String... message) {
      super(ErrorCodes.ENTITY_NOT_FOUND(Arrays.stream(message).toList().toString()));
    }
  }

  public static class ArticleNotCreate extends InvalidInputException {
    public ArticleNotCreate(String... message) {
      super(ErrorCodes.INVALID_UPDATE(Arrays.stream(message)
        .toList()
        .toString()));
    }
  }

  public static class ArticleNotUpdate extends InvalidInputException {
    public ArticleNotUpdate(String... message) {
      super(ErrorCodes.INVALID_UPDATE(Arrays.stream(message)
        .toList()
        .toString()));
    }
  }

}

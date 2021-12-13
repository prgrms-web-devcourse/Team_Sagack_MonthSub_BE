package com.prgrms.monthsub.common.exception.global;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;

public class S3UploaderException {

  public static class ImageExtensionNotMatch extends BusinessException {
    public ImageExtensionNotMatch() {
      super(ErrorCodes.INVALID_UPLOAD_FILE_TYPE());
    }
  }

  public static class ReadyToUploadError extends BusinessException {
    public ReadyToUploadError(String message) {
      super(ErrorCodes.FILE_ERROR(message));
    }
  }

  public static class UploadError extends BusinessException {
    public UploadError(String message) {
      super(ErrorCodes.FILE_ERROR(message));
    }
  }

  public static class DeleteError extends BusinessException {
    public DeleteError(String message) {
      super(ErrorCodes.FILE_ERROR(message));
    }
  }

}

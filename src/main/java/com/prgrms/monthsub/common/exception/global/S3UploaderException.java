package com.prgrms.monthsub.common.exception.global;

import com.prgrms.monthsub.common.exception.base.BusinessException;
import com.prgrms.monthsub.common.exception.model.ErrorCodes;

public class S3UploaderException {

    public static class ImageExtensionNotMatch extends BusinessException {

        public ImageExtensionNotMatch() {
            super(ErrorCodes.INVALID_UPLOAD_FILE_TYPE());
        }

    }

}

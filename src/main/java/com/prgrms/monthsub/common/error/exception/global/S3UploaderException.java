package com.prgrms.monthsub.common.error.exception.global;

import com.prgrms.monthsub.common.error.ErrorCodes;

public class S3UploaderException {

    public static class ImageExtensionNotMatch extends BusinessException {

        public ImageExtensionNotMatch() {
            super(ErrorCodes.INVALID_UPLOAD_FILE_TYPE());
        }

    }

}

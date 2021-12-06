package com.prgrms.monthsub.part.user.app.inferface;

import com.prgrms.monthsub.part.writer.domain.Writer;

public interface WriterProvider {
    Writer findByUserId(Long userId);
}

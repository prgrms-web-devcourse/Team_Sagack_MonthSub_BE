package com.prgrms.monthsub.module.part.user.app.inferface;

import com.prgrms.monthsub.module.part.writer.domain.Writer;

public interface WriterProvider {

  Writer findByUserId(Long userId);

}

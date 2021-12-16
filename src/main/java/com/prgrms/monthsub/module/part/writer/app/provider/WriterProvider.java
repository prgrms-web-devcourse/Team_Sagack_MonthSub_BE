package com.prgrms.monthsub.module.part.writer.app.provider;

import com.prgrms.monthsub.module.part.writer.domain.Writer;

public interface WriterProvider {

  Writer findByUserId(Long userId);

  Writer findById(Long id);

}

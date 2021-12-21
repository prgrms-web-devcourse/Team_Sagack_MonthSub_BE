package com.prgrms.monthsub.module.part.writer.app.provider;

import com.prgrms.monthsub.module.part.writer.domain.Writer;
import java.util.Optional;

public interface WriterProvider {

  Writer findByUserId(Long userId);

  Writer findById(Long id);

  Optional<Writer> findByUserIdOrEmpty(Long userId);

  }

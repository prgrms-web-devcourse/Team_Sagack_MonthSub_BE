package com.prgrms.monthsub.module.worker.expulsion.app.provider;

import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.DomainType;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.FileType;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.Status;

public interface ExpulsionProvider {

  void save(
    Long domainId,
    Long userId,
    String originalKey,
    Status status,
    DomainType domainType,
    FileCategory fileCategory,
    FileType fileType
  );

}

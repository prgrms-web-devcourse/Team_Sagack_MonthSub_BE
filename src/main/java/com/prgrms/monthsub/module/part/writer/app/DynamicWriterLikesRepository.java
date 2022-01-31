package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import java.time.LocalDateTime;
import java.util.List;

public interface DynamicWriterLikesRepository {

  List<WriterLikes> findAll(
    Long userId,
    Long lastId,
    int size,
    LocalDateTime createdAt
  );

}

package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WriterLikesService {

  private final WriterLikesRepository writerLikesRepository;

  public WriterLikesService(WriterLikesRepository writerLikesRepository) {
    this.writerLikesRepository = writerLikesRepository;
  }

  public List<WriterLikes> findAllByUserId(
    Long userId,
    LikesStatus likesStatus
  ) {
    return this.writerLikesRepository
      .findAllByUserId(userId, likesStatus);
  }

}

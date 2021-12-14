package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.part.writer.dto.WriterFollowEvent;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WriterLikesService {

  private final int INCREASE_NUM = 1;
  private final int DECREASE_NUM = -1;

  private final WriterLikesRepository writerLikesRepository;
  private final WriterProvider writerProvider;

  public WriterLikesService(
    WriterLikesRepository writerLikesRepository,
    WriterProvider writerProvider
  ) {
    this.writerLikesRepository = writerLikesRepository;
    this.writerProvider = writerProvider;
  }

  public List<WriterLikes> findAllByUserIdAndAndLikesStatus(
    Long userId,
    LikesStatus likesStatus
  ) {
    return this.writerLikesRepository.findAllByUserIdAndLikesStatus(userId, likesStatus);
  }

  @Transactional
  public WriterFollowEvent.Response likesEvent(
    Long userId,
    Long writerId
  ) {
    return this.writerLikesRepository.findByUserIdAndWriterId(userId, writerId)
      .map(writerLikes -> {
        String likeStatus = String.valueOf(writerLikes.changeLikeStatus(LikesStatus.Like));
        writerLikes.getWriter()
          .updateFollowCount(INCREASE_NUM);
        return new WriterFollowEvent.Response(
          this.writerLikesRepository.save(writerLikes)
            .getId(), likeStatus);
      })
      .orElseGet(() -> {
        Writer writer = this.writerProvider.findById(writerId);
        writer.updateFollowCount(INCREASE_NUM);
        return new WriterFollowEvent.Response(this.writerLikesRepository.save(WriterLikes.builder()
            .likesStatus(LikesStatus.Like)
            .writer(writer)
            .userId(userId)
            .build())
          .getId(), String.valueOf(LikesStatus.Like));
      });
  }

  @Transactional
  public WriterFollowEvent.Response cancelLikesEvent(
    Long userId,
    Long writerId
  ) {
    return this.writerLikesRepository.findByUserIdAndWriterId(userId, writerId)
      .map(writerLikes -> {
        String likeStatus = String.valueOf(writerLikes.changeLikeStatus(LikesStatus.Nothing));
        return new WriterFollowEvent.Response(
          this.writerLikesRepository.save(writerLikes)
            .getId(), likeStatus);
      })
      .get();
  }


}

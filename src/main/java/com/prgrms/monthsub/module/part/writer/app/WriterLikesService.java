package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.part.writer.dto.WriterFollowEvent;
import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WriterLikesService {

  private final int INCREASE_NUM = 1;
  private final int DECREASE_NUM = -1;
  private final int DEFAULT_WRITER_LIKES = 10;

  private final WriterLikesRepository writerLikesRepository;
  private final WriterProvider writerProvider;
  private final WriterConverter writerConverter;

  public WriterLikesService(
    WriterLikesRepository writerLikesRepository,
    WriterProvider writerProvider,
    WriterConverter writerConverter
  ) {
    this.writerLikesRepository = writerLikesRepository;
    this.writerProvider = writerProvider;
    this.writerConverter = writerConverter;
  }

  public List<WriterLikes> findAllByUserIdAndAndLikesStatus(
    Long userId,
    LikesStatus likesStatus
  ) {
    PageRequest cursorPageable = getPageRequest(DEFAULT_WRITER_LIKES);

    return this.writerLikesRepository.findAllByUserIdAndLikesStatus(
      userId, likesStatus, cursorPageable);
  }

  @Transactional
  public WriterLikesList.Response getWriterLikesList(
    Long userId,
    Long lastId,
    Integer size
  ) {
    PageRequest cursorPageable = getPageRequest(size);

    return new WriterLikesList.Response((
      (lastId == null) ? this.writerLikesRepository
        .findAllByUserIdAndLikesStatus(userId, LikesStatus.Like, cursorPageable)
        : this.writerLikesRepository.findByIdGreaterThanAndUserIdAndLikesStatus(
          lastId, userId, LikesStatus.Like, cursorPageable)
    ).stream()
      .map(WriterLikes::getWriter)
      .map(this.writerConverter::writerLikesToWriterLikesList)
      .collect(Collectors.toList()));
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
        Writer writer = this.writerProvider.findWriterByUserId(writerId);
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
        writerLikes.getWriter()
          .updateFollowCount(DECREASE_NUM);
        return new WriterFollowEvent.Response(
          this.writerLikesRepository.save(writerLikes)
            .getId(), likeStatus);
      })
      .get();
  }

  private PageRequest getPageRequest(int size) {
    return PageRequest.of(
      0,
      size,
      Sort.by(Direction.DESC, "updateAt", "id")
    );
  }

}

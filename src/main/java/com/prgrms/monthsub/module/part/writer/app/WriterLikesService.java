package com.prgrms.monthsub.module.part.writer.app;

import static java.util.Optional.ofNullable;

import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterNotFound;
import com.prgrms.monthsub.module.part.writer.dto.WriterFollowEvent;
import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList;
import java.util.List;
import java.util.Optional;
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

  private final WriterLikesRepository writerLikesRepository;
  private final WriterService writerService;
  private final WriterConverter writerConverter;

  public WriterLikesService(
    WriterLikesRepository writerLikesRepository,
    WriterService writerService,
    WriterConverter writerConverter
  ) {
    this.writerLikesRepository = writerLikesRepository;
    this.writerService = writerService;
    this.writerConverter = writerConverter;
  }

  @Transactional
  public WriterLikesList.Response getWriterLikesList(
    Long channelOwnerUserId,
    Long lastId,
    Integer size
  ) {
    PageRequest cursorPageable = getPageRequest(size);

    return new WriterLikesList.Response(
      this.getWriterLikes(channelOwnerUserId, ofNullable(lastId), cursorPageable)
        .stream()
        .map(this.writerConverter::toWriterLikesList)
        .collect(Collectors.toList())
    );
  }

  public List<WriterLikes> getFollowWriterList(
    Long userId,
    LikesStatus likesStatus
  ) {
    return this.writerLikesRepository.findAllByUserIdAndLikesStatus(userId, likesStatus);
  }

  private List<WriterLikes> getWriterLikes(
    Long userId,
    Optional<Long> lastId,
    PageRequest cursorPageable
  ) {
    return lastId.map(lastWriterLikesId ->
        this.writerLikesRepository.findByIdLessThanAndUserIdAndLikesStatus(
          lastWriterLikesId, userId, LikesStatus.Like, cursorPageable
        )
      )
      .orElse(
        this.writerLikesRepository.findAllByUserIdAndLikesStatus(
          userId, LikesStatus.Like, cursorPageable)
      );
  }

  @Transactional
  public WriterFollowEvent.Response likesEvent(
    Long userId,
    Long writerId
  ) {
    return this.writerLikesRepository
      .findByUserIdAndWriterId(userId, writerId)
      .map(writerLikes -> {
        String likeStatus = String.valueOf(writerLikes.changeLikeStatus(LikesStatus.Like));

        writerLikes.getWriter().updateFollowCount(INCREASE_NUM);

        return new WriterFollowEvent.Response(
          this.writerLikesRepository.save(writerLikes).getUserId(),
          likeStatus
        );
      })
      .orElseGet(() -> {
          Writer writer = this.writerService.findById(writerId);
          writer.updateFollowCount(INCREASE_NUM);

          return new WriterFollowEvent.Response(
            this.writerLikesRepository.save(
              WriterLikes.builder()
                .likesStatus(LikesStatus.Like)
                .writer(writer)
                .userId(userId)
                .build()).getUserId(),
            String.valueOf(LikesStatus.Like)
          );
        }
      );
  }

  @Transactional
  public WriterFollowEvent.Response cancelLikesEvent(
    Long userId,
    Long writerId
  ) {
    WriterLikes writerLikes = getByUserIdAndWriterId(userId, writerId);
    String likeStatus = String.valueOf(writerLikes.changeLikeStatus(LikesStatus.Nothing));

    writerLikes.getWriter().updateFollowCount(DECREASE_NUM);

    return new WriterFollowEvent.Response(
      this.writerLikesRepository.save(writerLikes).getId(),
      likeStatus
    );
  }

  private WriterLikes getByUserIdAndWriterId(
    Long userId,
    Long writerId
  ) {
    return this.writerLikesRepository
      .findByUserIdAndWriterId(userId, writerId)
      .orElseThrow(() ->
        new WriterNotFound("userId=" + userId + ", " + "writerId=", writerId.toString())
      );
  }

  private PageRequest getPageRequest(int size) {
    return PageRequest.of(
      0,
      size,
      Sort.by(Direction.DESC, "updateAt", "id")
    );
  }

}

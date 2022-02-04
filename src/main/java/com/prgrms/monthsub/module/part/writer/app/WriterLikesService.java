package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterLikesNotFound;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterNotFound;
import com.prgrms.monthsub.module.part.writer.dto.WriterFollowEvent;
import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList;
import java.time.LocalDateTime;
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

  private final CustomWriterLikesRepository customWriterLikesRepository;
  private final WriterService writerService;
  private final WriterConverter writerConverter;

  public WriterLikesService(
    CustomWriterLikesRepository customWriterLikesRepository,
    WriterService writerService,
    WriterConverter writerConverter
  ) {
    this.customWriterLikesRepository = customWriterLikesRepository;
    this.writerService = writerService;
    this.writerConverter = writerConverter;
  }

  public WriterLikes getById(Long id) {
    return this.customWriterLikesRepository
      .findById(id)
      .orElseThrow(() -> new WriterLikesNotFound("id=" + id));
  }

  public WriterLikesList.Response getWriterLikesList(
    Long userId,
    Optional<Long> lastId,
    Integer size
  ) {
    return new WriterLikesList.Response(lastId.map(id -> {
          LocalDateTime createdAt = this.getById(id).getCreatedAt();
          return this.customWriterLikesRepository.findAll(
            userId, id, size, createdAt
          );
        }
      )
      .orElse(
        this.customWriterLikesRepository.findAllByUserIdAndLikesStatus(
          userId,
          LikesStatus.Like,
          PageRequest.of(0, size, Sort.by(Direction.DESC, "createdAt", "id"))
        )
      )
      .stream()
      .map(this.writerConverter::toWriterLikesList)
      .collect(Collectors.toList())
    );
  }

  public List<WriterLikes> getFollowWriterList(
    Long userId,
    LikesStatus likesStatus
  ) {
    return this.customWriterLikesRepository.findAllByUserIdAndLikesStatus(userId, likesStatus);
  }

  @Transactional
  public WriterFollowEvent.Response likesEvent(
    Long userId,
    Long writerId
  ) {
    return this.customWriterLikesRepository
      .findByUserIdAndWriterId(userId, writerId)
      .map(writerLikes -> {
        String likeStatus = String.valueOf(writerLikes.changeLikeStatus(LikesStatus.Like));

        writerLikes.getWriter().updateFollowCount(INCREASE_NUM);

        return new WriterFollowEvent.Response(
          this.customWriterLikesRepository.save(writerLikes).getUserId(),
          likeStatus
        );
      })
      .orElseGet(() -> {
          Writer writer = this.writerService.findById(writerId);
          writer.updateFollowCount(INCREASE_NUM);

          return new WriterFollowEvent.Response(
            this.customWriterLikesRepository.save(
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
      this.customWriterLikesRepository.save(writerLikes).getId(),
      likeStatus
    );
  }

  private WriterLikes getByUserIdAndWriterId(
    Long userId,
    Long writerId
  ) {
    return this.customWriterLikesRepository
      .findByUserIdAndWriterId(userId, writerId)
      .orElseThrow(() ->
        new WriterNotFound("userId=" + userId + ", " + "writerId=", writerId.toString())
      );
  }

}

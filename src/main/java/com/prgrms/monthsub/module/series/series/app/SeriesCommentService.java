package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.series.series.converter.SeriesCommentConverter;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesCommentNotFound;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SeriesCommentService {

  private final SeriesCommentRepository seriesCommentRepository;
  private final SeriesCommentConverter seriesCommentConverter;
  private final UserProvider userProvider;

  public SeriesCommentService(
    SeriesCommentRepository seriesCommentRepository,
    SeriesCommentConverter seriesCommentConverter,
    UserProvider userProvider
  ) {
    this.seriesCommentRepository = seriesCommentRepository;
    this.seriesCommentConverter = seriesCommentConverter;
    this.userProvider = userProvider;
  }

  public SeriesCommentList.Response getComments(
    Optional<Long> userIdOrEmpty,
    Long seriesId
  ) {
    List<SeriesComment> comments = this.seriesCommentRepository.findAllBySeriesIdAndParentIdIsNull(
      seriesId
    );
    List<SeriesComment> replyComments = this.seriesCommentRepository.findAllBySeriesIdAndParentIdIsNotNull(
      seriesId
    );

    List<Long> userIds = Stream.concat(comments.stream(), replyComments.stream())
      .map(SeriesComment::getUserId)
      .distinct()
      .collect(Collectors.toList());
    List<User> users = this.userProvider.findByIdIn(userIds);

    return this.seriesCommentConverter.toResponse(userIdOrEmpty, comments, replyComments, users);
  }

  public SeriesCommentPost.Response saveComment(
    Long userId,
    SeriesCommentPost.Request request
  ) {
    return SeriesCommentPost.Response.builder()
      .id(
        this.seriesCommentRepository.save(
            this.seriesCommentConverter.toEntity(userId, request)
          )
          .getId()
      )
      .build();
  }

  public SeriesCommentEdit.Response editComment(
    Long userId,
    SeriesCommentEdit.Request request
  ) {
    SeriesComment seriesComment = this.seriesCommentRepository.findById(request.id())
      .orElseThrow(() -> new SeriesCommentNotFound("id= " + request.id()));

    if (!seriesComment.isMine(userId)) {
      final String message = "seriesCommentId=" + seriesComment.getId() + ", userId=" + userId;
      throw new AccessDeniedException(message + ":수정 권한이 없습니다.");
    }

    seriesComment.editComment(request.comment());

    return SeriesCommentEdit.Response.builder()
      .id(seriesComment.getId())
      .isMine(true)
      .build();
  }

  public void deleteComment(
    Long userId,
    Long id
  ) {
    SeriesComment seriesComment = this.seriesCommentRepository.findById(id)
      .orElseThrow(() -> new SeriesCommentNotFound("id= " + id));

    if (!seriesComment.isMine(userId)) {
      final String message = "seriesCommentId=" + seriesComment.getId() + ", userId=" + userId;
      throw new AccessDeniedException(message + ":삭제 권한이 없습니다.");
    }

    seriesComment.deleteComment();
  }

}

package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.converter.SeriesCommentConverter;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesCommentNotFound;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SeriesCommentService {

  private final SeriesCommentRepository seriesCommentRepository;
  private final SeriesCommentConverter seriesCommentConverter;

  public SeriesCommentService(
    SeriesCommentRepository seriesCommentRepository,
    SeriesCommentConverter seriesCommentConverter
  ) {
    this.seriesCommentRepository = seriesCommentRepository;
    this.seriesCommentConverter = seriesCommentConverter;
  }

  public SeriesCommentPost.Response saveComment(
    Long userId,
    SeriesCommentPost.Request request
  ) {
    return SeriesCommentPost.Response.builder()
      .id(
        this.seriesCommentRepository.save(
          this.seriesCommentConverter.toEntity(userId, request)
        ).getId()
      ).build();
  }

  public SeriesCommentEdit.Response editComment(
    Long userId,
    SeriesCommentEdit.Request request
  ) {
    SeriesComment seriesComment = this.seriesCommentRepository.findById(request.id())
      .orElseThrow(() -> new SeriesCommentNotFound("id= " + request.id()));
    if(!seriesComment.isMine(userId)){
      throw new AccessDeniedException("수정 권한이 없습니다.");
    }

    seriesComment.editComment(request.comment());

    return SeriesCommentEdit.Response.builder()
      .id(seriesComment.getId())
      .isMine(true)
      .build();
  }

  public void deleteComment(Long userId, Long id){
    SeriesComment seriesComment = this.seriesCommentRepository.findById(id)
      .orElseThrow(() -> new SeriesCommentNotFound("id= " + id));
    if(!seriesComment.isMine(userId)){
      throw new AccessDeniedException("삭제 권한이 없습니다.");
    }

    seriesComment.deleteComment();
  }
  
}

package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment.CommentStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import org.springframework.stereotype.Component;

@Component
public class SeriesCommentConverter {

  public SeriesCommentConverter(){
  }

  public SeriesComment toEntity(
    Long userId,
    SeriesCommentPost.Request request
  ){
    return SeriesComment.builder()
      .comment(request.comment())
      .userId(userId)
      .seriesId(request.seriesId())
      .parentId(request.parentId())
      .commentStatus(CommentStatus.CREATED)
      .build();
  }

}

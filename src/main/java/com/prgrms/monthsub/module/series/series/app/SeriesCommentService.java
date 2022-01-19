package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.converter.SeriesCommentConverter;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import org.springframework.stereotype.Service;

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

  public SeriesCommentPost.Response save(
    Long userId,
    SeriesCommentPost.Request request
  ) {
    return SeriesCommentPost.Response.builder()
      .id(
        this.seriesCommentRepository.save(
          seriesCommentConverter.toEntity(userId, request)
        ).getId()
      ).build();
  }

}

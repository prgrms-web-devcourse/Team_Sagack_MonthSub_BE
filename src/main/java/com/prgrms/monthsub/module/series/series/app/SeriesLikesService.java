package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes.LikesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesLikesNotFound;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesEvent;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeriesLikesService {
  private final SeriesLikesRepository seriesLikesRepository;
  private final SeriesService seriesService;

  public SeriesLikesService(
    SeriesLikesRepository seriesLikesRepository,
    SeriesService seriesService
  ) {
    this.seriesLikesRepository = seriesLikesRepository;
    this.seriesService = seriesService;
  }

  @Transactional
  public SeriesLikesEvent.Response likesEvent(
    Long userId,
    Long seriesId
  ) {
    return this.seriesLikesRepository
      .findSeriesLikesByUserIdAndSeriesId(userId, seriesId)
      .map(seriesLikes -> {
          LikesStatus changeStatus = seriesLikes.changeLikeStatus();

          seriesLikes
            .getSeries()
            .changeLikesCount(changeStatus);

          return new SeriesLikesEvent.Response(
            this.seriesLikesRepository
              .save(seriesLikes)
              .getId(),
            String.valueOf(changeStatus)
          );
        }
      )
      .orElseGet(() -> {
          Series seriesEntity = this.seriesService.getById(seriesId);
          seriesEntity.changeLikesCount(LikesStatus.Like);

          SeriesLikes seriesLikesEntity = SeriesLikes.builder()
            .userId(userId)
            .series(seriesEntity)
            .likesStatus(LikesStatus.Like)
            .build();

          return new SeriesLikesEvent.Response(
            this.seriesLikesRepository
              .save(seriesLikesEntity)
              .getId(),
            String.valueOf(LikesStatus.Like)
          );
        }
      );
  }

  @Transactional
  public SeriesLikesEvent.Response cancelSeriesLike(
    Long userId,
    Long seriesId
  ) {
    return this.seriesLikesRepository
      .findSeriesLikesByUserIdAndSeriesId(userId, seriesId)
      .map(seriesLikes -> {
          SeriesLikes.LikesStatus changeStatus = seriesLikes.changeLikeStatus();

          seriesLikes
            .getSeries()
            .changeLikesCount(changeStatus);

          return new SeriesLikesEvent.Response(
            this.seriesLikesRepository
              .save(seriesLikes)
              .getId(),
            String.valueOf(changeStatus)
          );
        }
      )
      .orElseThrow(() -> new SeriesLikesNotFound("userId=" + userId, "seriesId=" + seriesId));
  }

  @Transactional(readOnly = true)
  public List<SeriesLikes> findAllMySeriesLikeByUserId(Long userId) {
    return this.seriesLikesRepository.findAllMySeriesLikeByUserId(userId);

  }
}

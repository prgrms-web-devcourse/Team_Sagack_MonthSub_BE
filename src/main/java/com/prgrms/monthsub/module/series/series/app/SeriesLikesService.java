package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes.LikesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesLikesNotFound;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesEvent;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeriesLikesService {

  private final SeriesLikesRepository seriesLikesRepository;
  private final SeriesService seriesService;
  private final SeriesConverter seriesConverter;

  public SeriesLikesService(
    SeriesLikesRepository seriesLikesRepository,
    SeriesService seriesService,
    SeriesConverter seriesConverter
  ) {
    this.seriesLikesRepository = seriesLikesRepository;
    this.seriesService = seriesService;
    this.seriesConverter = seriesConverter;
  }

  @Transactional
  public SeriesLikesEvent.Response likesEvent(
    Long userId,
    Long seriesId
  ) {
    return this.seriesLikesRepository
      .findSeriesLikesByUserIdAndSeriesId(userId, seriesId)
      .map(seriesLikes -> {
          LikesStatus changeStatus = seriesLikes.changeLikeStatus(LikesStatus.Like);

          seriesLikes.getSeries().changeLikesCount(changeStatus);

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
          SeriesLikes.LikesStatus changeStatus = seriesLikes.changeLikeStatus(LikesStatus.Nothing);

          seriesLikes.getSeries().changeLikesCount(changeStatus);

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
  public SeriesLikesList.Response findAllMySeriesLikeByUserId(Long userId) {
    return new SeriesLikesList.Response(
      this.seriesLikesRepository.findAllByUserIdAndLikesStatus(userId, LikesStatus.Like)
        .stream()
        .map(seriesLikes -> {
          seriesLikes.getSeries().changeSeriesIsLiked(true);
          return seriesLikes.getSeries();
        })
        .map(this.seriesConverter::toResponse)
        .collect(Collectors.toList())
    );
  }

  @Transactional(readOnly = true)
  public List<Long> findAllByUserId(Long userId) {
    return this.seriesLikesRepository.findAllByUserIdAndLikesStatus(userId, LikesStatus.Like)
      .stream()
      .map(seriesLikes -> seriesLikes.getSeries().getId())
      .collect(Collectors.toList());
  }

}

package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes.LikesStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesLikesList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SeriesLikesService {

  private final SeriesLikesRepository seriesLikesRepository;
  private final SeriesConverter seriesConverter;

  public SeriesLikesService(
    SeriesLikesRepository seriesLikesRepository,
    SeriesConverter seriesConverter
  ) {
    this.seriesLikesRepository = seriesLikesRepository;
    this.seriesConverter = seriesConverter;
  }

  @Transactional
  public Long save(SeriesLikes seriesLikes){
    return this.seriesLikesRepository.save(seriesLikes).getId();
  }

  @Transactional(readOnly = true)
  public Optional<SeriesLikes> findSeriesLikes(Long userId, Long seriesId){
    return this.seriesLikesRepository.findSeriesLikesByUserIdAndSeriesId(userId, seriesId);
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

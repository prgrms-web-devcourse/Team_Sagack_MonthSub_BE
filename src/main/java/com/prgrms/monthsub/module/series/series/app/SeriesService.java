package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeriesService {

  private final SeriesRepository seriesRepository;

  public SeriesService(
    SeriesRepository seriesRepository
  ) {
    this.seriesRepository = seriesRepository;
  }

  public Series getById(Long id) {
    return this.seriesRepository
      .findById(id)
      .orElseThrow(() -> new SeriesNotFound("id=" + id));
  }

  @Transactional
  public Long save(Series series) {
    return this.seriesRepository.save(series)
      .getId();
  }

  public boolean checkSeriesStatusByWriterId(
    Long writerId,
    SeriesStatus status
  ) {
    return this.seriesRepository.existsAllByWriterIdAndSubscribeStatus(writerId, status);
  }

  public List<Series> findAllByWriterId(
    Long writerId
  ) {
    return this.seriesRepository.findAllByWriterId(writerId);
  }

  public List<Series> getSeriesSearchTitle(String title) {
    return this.seriesRepository.findByTitleContainingIgnoreCase(title);
  }

  public List<Series> findAll(Sort sort) {
    return this.seriesRepository.findAll(sort);
  }

}

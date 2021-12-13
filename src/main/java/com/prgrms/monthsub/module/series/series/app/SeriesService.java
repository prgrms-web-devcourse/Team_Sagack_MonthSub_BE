package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeriesService implements SeriesProvider {

  private final SeriesRepository seriesRepository;
  private final ArticleUploadDateRepository articleUploadDateRepository;

  public SeriesService(
    SeriesRepository seriesRepository,
    ArticleUploadDateRepository articleUploadDateRepository
  ) {
    this.seriesRepository = seriesRepository;
    this.articleUploadDateRepository = articleUploadDateRepository;
  }

  @Override
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

  @Transactional
  public void articleUploadDateSave(ArticleUploadDate articleUploadDate) {
    this.articleUploadDateRepository.save(articleUploadDate);
  }

  @Override
  public List<ArticleUploadDate> getArticleUploadDate(Long seriesId) {
    return this.articleUploadDateRepository.findAllBySeriesId(seriesId);
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

  public List<Series> findAll(Pageable pageable) {
    return this.seriesRepository
      .findAll(pageable)
      .getContent();
  }

  public List<Series> getSeries(
    Long id,
    Pageable pageable
  ) {
    return this.seriesRepository.findByIdLessThan(id, pageable);
  }

}

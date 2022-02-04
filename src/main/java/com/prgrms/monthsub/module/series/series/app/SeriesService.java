package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.scheduler.ScheduledHandler;
import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeriesService implements SeriesProvider, ScheduledHandler {

  private final CustomSeriesRepository seriesRepository;
  private final ArticleUploadDateRepository articleUploadDateRepository;
  private final Logger logger = LoggerFactory.getLogger(getClass());

  public SeriesService(
    CustomSeriesRepository seriesRepository,
    SeriesConverter seriesConverter,
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
    return this.seriesRepository.save(series).getId();
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
    return this.seriesRepository.findAll(pageable).getContent();
  }

  public List<Series> findBySubscribeStatus(
    SeriesStatus status,
    Pageable pageable
  ) {
    return this.seriesRepository.findBySubscribeStatus(status, pageable);
  }

  public List<Series> findAllByCategory(
    Long lastSeriesId,
    int size,
    List<Category> categories,
    LocalDateTime createdAt
  ) {
    return this.seriesRepository.findAllByCategory(lastSeriesId, size, categories, createdAt);
  }

  public List<Series> findAllByCategoryIn(
    List<Category> categories,
    Pageable pageable
  ) {
    return this.seriesRepository.findAllByCategoryIn(categories, pageable);
  }

  @Transactional
  @Override
  public void changeSeriesStatus() {
    AtomicInteger editCount = new AtomicInteger();

    LocalDate today = LocalDate.now();
    List<Series> seriesList = this.seriesRepository.findAll()
      .stream()
      .peek(series -> {
        SeriesStatus before = series.getSubscribeStatus();
        series.changeSeriesStatus(today);
        SeriesStatus after = series.getSubscribeStatus();
        if (!before.equals(after)) {
          logger.info("시리즈 ID :{}, 상태 값 {} -> {} 변경 ", series.getId(), before, after);
        }
        editCount.getAndIncrement();
      })
      .collect(Collectors.toList());
    logger.info("확인 된 Series 갯 수 : {}", editCount);
    this.seriesRepository.saveAll(seriesList);
  }

}

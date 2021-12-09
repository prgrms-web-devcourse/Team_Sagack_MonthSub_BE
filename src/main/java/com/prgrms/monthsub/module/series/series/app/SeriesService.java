package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.series.article.app.ArticleService;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeriesService {

  private final SeriesRepository seriesRepository;
  private final ArticleService articleService;
  private final ExpulsionService expulsionService;
  private final WriterProvider writerProvider;
  private final UserProvider userProvider;
  private final S3Uploader s3Uploader;
  private final SeriesConverter seriesConverter;

  public SeriesService(
    SeriesRepository seriesRepository,
    ArticleService articleService,
    ExpulsionService expulsionService,
    WriterService writerProvider,
    UserService userProvider,
    SeriesConverter seriesConverter,
    S3Uploader s3Uploader
  ) {
    this.seriesRepository = seriesRepository;
    this.articleService = articleService;
    this.expulsionService = expulsionService;
    this.writerProvider = writerProvider;
    this.userProvider = userProvider;
    this.seriesConverter = seriesConverter;
    this.s3Uploader = s3Uploader;
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

  public List<Series> getSeriesList() {
    return this.seriesRepository.findSeriesList();
  }

  public List<Series> findSeriesListOrderByCreatedAt() {
    return this.seriesRepository.findSeriesListOrderByCreatedAt();
  }

  public List<Series> findSeriesListOrderByLike() {
    return this.seriesRepository.findSeriesListOrderByLike();
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

}

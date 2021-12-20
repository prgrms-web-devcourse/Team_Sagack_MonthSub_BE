package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.converter.MainPageConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.dto.MainPage;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MainPageAssemble {

  private final int PAGE_NUM = 0;
  private final int POPULAR_SERIES_SIZE = 10;
  private final int PAGE_WRITER_NUM = 10;

  private final SeriesService seriesService;
  private final WriterService writerService;
  private final MainPageConverter mainPageConverter;
  private final SeriesLikesService seriesLikesService;

  public MainPageAssemble(
    SeriesService seriesService,
    WriterService writerService,
    MainPageConverter mainPageConverter,
    SeriesLikesService seriesLikesService
  ) {
    this.seriesService = seriesService;
    this.writerService = writerService;
    this.mainPageConverter = mainPageConverter;
    this.seriesLikesService = seriesLikesService;
  }

  public MainPage.Response getMainPage(
    Optional<Long> userIdOrEmpty
  ) {
    List<Long> likeSeriesList = userIdOrEmpty.map(
        this.seriesLikesService::findAllByUserId
      )
      .orElse(Collections.emptyList());

    List<Series> popularSeriesList = this.seriesService.findAll(PageRequest.of(
      PAGE_NUM,
      POPULAR_SERIES_SIZE,
      Sort.by(Direction.DESC, "likes")
    ));

    List<Writer> popularWriterList = this.writerService.findAll(
      PageRequest.of(PAGE_NUM, PAGE_WRITER_NUM, Sort.by(Direction.DESC, "followCount")));

    List<Series> recentSeriesList = this.seriesService.findBySubscribeStatus(
        SeriesStatus.SUBSCRIPTION_AVAILABLE,
        PageRequest.of(PAGE_NUM, POPULAR_SERIES_SIZE, Sort.by(Direction.DESC, "createdAt", "id"))
      )
      .stream()
      .peek(series -> {
        if (likeSeriesList.contains(series.getId())) {
          series.changeSeriesIsLiked(true);
        }
      })
      .collect(Collectors.toList());

    return this.mainPageConverter.toResponse(
      popularSeriesList, popularWriterList, recentSeriesList
    );
  }

}

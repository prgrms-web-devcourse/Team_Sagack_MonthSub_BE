package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.converter.MainPageConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.dto.MainPage;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MainPageAssemble {

  private final int PAGE_NUM = 0;
  private final int PAGE_SIZE = 8;

  private final SeriesService seriesService;
  private final WriterService writerService;
  private final SeriesUserService seriesUserService;
  private final MainPageConverter mainPageConverter;

  public MainPageAssemble(
    SeriesService seriesService,
    WriterService writerService,
    SeriesUserService seriesUserService,
    MainPageConverter mainPageConverter
  ) {
    this.seriesService = seriesService;
    this.writerService = writerService;
    this.seriesUserService = seriesUserService;
    this.mainPageConverter = mainPageConverter;
  }

  public MainPage.Response getMainPage() {

    List<Series> popularSeriesList = this.seriesService.findAll(PageRequest.of(
      PAGE_NUM,
      PAGE_SIZE,
      Sort.by(Direction.DESC, "likes")
    ));

    List<Writer> popularWriterList = this.writerService.findAll(
      PageRequest.of(PAGE_NUM, PAGE_SIZE + 2, Sort.by(Direction.DESC, "followCount")));

    List<Series> recentSeriesList = this.seriesService.findBySubscribeStatus(
      SeriesStatus.SUBSCRIPTION_AVAILABLE,
      PageRequest.of(PAGE_NUM, PAGE_SIZE, Sort.by(Direction.DESC, "createdAt", "id"))
    );

    return this.mainPageConverter.MainPageToResponse(
      popularSeriesList, popularWriterList, recentSeriesList
    );

  }

}

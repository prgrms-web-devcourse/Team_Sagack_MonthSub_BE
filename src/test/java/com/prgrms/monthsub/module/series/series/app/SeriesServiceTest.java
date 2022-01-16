package com.prgrms.monthsub.module.series.series.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate.UploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.util.ReflectionTestUtils;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {
  private final int PAGE_NUM = 0;
  private final int SERIES_SIZE = 10;
  private final long WRITER_ID = 1L;

  @InjectMocks
  private SeriesService seriesService;

  @Mock
  private SeriesRepositoryCustom seriesRepository;

  @Mock
  private ArticleUploadDateRepository articleUploadDateRepository;

  public Series getSeriesFixture(
    Long id,
    Long writerId
  ) {
    Writer writer = getWriterFixture(writerId);
    Series series = Series.builder()
      .title("타이틀" + id)
      .writer(writer)
      .subscribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE)
      .category(Category.ESSAY)
      .price(1000)
      .articleCount(10)
      .introduceSentence("소개문장")
      .build();
    ReflectionTestUtils.setField(series, "id", id);
    ReflectionTestUtils.setField(series, "createdAt", LocalDateTime.now());

    return series;
  }

  public Writer getWriterFixture(Long id) {
    Writer writer = Writer.builder()
      .followCount((int) Math.pow(id, 2))
      .build();
    ReflectionTestUtils.setField(writer, "id", id);

    return writer;
  }

  public ArticleUploadDate getArticleUploadDateFixture(
    Long id,
    Long seriesId
  ) {
    ArticleUploadDate articleUploadDate = ArticleUploadDate.builder()
      .seriesId(seriesId)
      .uploadDate(UploadDate.FRIDAY)
      .build();
    ReflectionTestUtils.setField(articleUploadDate, "id", id);

    return articleUploadDate;
  }

  @Test
  @DisplayName("시리즈를 저장할 수 있다.")
  void saveTest() {
    //given
    Series series = getSeriesFixture(3L, WRITER_ID);
    doReturn(series).when(this.seriesRepository).save(any());

    //when
    Long id = this.seriesService.save(series);

    //then
    verify(seriesRepository, times(1)).save(any(Series.class));
    assertThat(id).isEqualTo(series.getId());
  }

  @Test
  @DisplayName("시리즈 단건 조회할 수 있다.")
  void getByIdTest() {
    //given
    Series series = getSeriesFixture(3L, WRITER_ID);
    doReturn(Optional.ofNullable(series)).when(this.seriesRepository).findById(any(Long.class));

    //when
    Series findSeries = this.seriesService.getById(3L);

    //then
    verify(seriesRepository, times(1)).findById(anyLong());
    assertThat(findSeries.getId()).isEqualTo(series.getId());
    assertThat(findSeries.getCategory()).isEqualTo(series.getCategory());
  }


  @Test
  @DisplayName("아티클 업로드 날짜를 저장할 수 있다.")
  void articleUploadDateSaveTest() {
    //given
    Series series = getSeriesFixture(3L, WRITER_ID);
    ArticleUploadDate articleUploadDate = getArticleUploadDateFixture(1L, series.getId());
    when(articleUploadDateRepository.save(any(ArticleUploadDate.class)))
      .thenReturn(articleUploadDate);

    //when
    this.seriesService.articleUploadDateSave(articleUploadDate);

    //then
    verify(articleUploadDateRepository, times(1)).save(any(ArticleUploadDate.class));
  }

  @Test
  @DisplayName("아티클 업로드 날짜를 가져올 수 있다.")
  void getArticleUploadDateTest() {
    //given
    Series series = getSeriesFixture(3L, WRITER_ID);
    List<ArticleUploadDate> articleUploadDateList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      articleUploadDateList.add(getArticleUploadDateFixture(i, series.getId()));
    }
    doReturn(articleUploadDateList)
      .when(this.articleUploadDateRepository)
      .findAllBySeriesId(any(Long.class));

    //when
    List<ArticleUploadDate> findList = this.seriesService.getArticleUploadDate(series.getId());

    //then
    verify(articleUploadDateRepository, times(1))
      .findAllBySeriesId(anyLong());
    assertThat(findList.size()).isEqualTo(articleUploadDateList.size());
  }

  @Test
  @DisplayName("특정 작가의 `입력된 모집 상태`와 같은 공고가 있는지 확인할 수 있다.")
  public void checkSeriesStatusByWriterIdTest() {
    //given
    Long writerId = WRITER_ID;
    SeriesStatus status = SeriesStatus.SUBSCRIPTION_AVAILABLE;
    Series series = getSeriesFixture(3L, writerId);
    doReturn(true)
      .when(this.seriesRepository)
      .existsAllByWriterIdAndSubscribeStatus(any(Long.class), any(SeriesStatus.class));

    //when
    boolean check = this.seriesService.checkSeriesStatusByWriterId(writerId, status);

    //then
    verify(seriesRepository, times(1))
      .existsAllByWriterIdAndSubscribeStatus(anyLong(), any(SeriesStatus.class));
    assertThat(check).isTrue();
    assertThat(series.getSubscribeStatus()).isEqualTo(status);
    assertThat(series.getWriter().getId()).isEqualTo(writerId);
  }

  @Test
  @DisplayName("특정 작가가 발행한 시리즈들을 조회할 수 있다.")
  void findAllByWriterIdTest() {
    //given
    Long writerId = WRITER_ID;
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    doReturn(seriesList)
      .when(this.seriesRepository)
      .findAllByWriterId(any(Long.class));

    //when
    List<Series> findSeriesList = this.seriesService.findAllByWriterId(writerId);

    //then
    verify(seriesRepository, times(1)).findAllByWriterId(anyLong());
    assertThat(findSeriesList.size()).isEqualTo(seriesList.size());
    findSeriesList
      .forEach(series -> {
        assertThat(series.getWriter().getId()).isEqualTo(writerId);
      });
  }

  @Test
  @DisplayName("입력된 제목을 가진 시리즈를 조회할 수 있다.")
  void getSeriesSearchTitle() {
    //given
    Long writerId = WRITER_ID;
    String title = "타이틀";
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    doReturn(seriesList)
      .when(this.seriesRepository)
      .findByTitleContainingIgnoreCase(anyString());

    //when
    List<Series> findSeriesList = this.seriesService.getSeriesSearchTitle(title);

    //then
    verify(seriesRepository, times(1)).findByTitleContainingIgnoreCase(anyString());
    assertThat(findSeriesList.size()).isEqualTo(seriesList.size());
    findSeriesList
      .forEach(series -> {
        assertThat(series.getTitle().contains(title)).isTrue();
      });
  }

  @Test
  @DisplayName("시리즈를 최신순으로 조회할 수 있다.")
  void recentSort_findAllTest() {
    //given
    Long writerId = WRITER_ID;
    Sort recentSort = Sort.by(Direction.DESC, "createdAt", "id");
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    List<Series> recentSeriesList = seriesList.stream()
      .sorted(
        Comparator
          .comparing(Series::getCreatedAt)
          .thenComparing(Series::getId)
          .reversed()
      )
      .collect(Collectors.toList());
    doReturn(recentSeriesList)
      .when(this.seriesRepository)
      .findAll(any(Sort.class));

    //when
    List<Series> findRecentSortList = this.seriesService.findAll(recentSort);

    //then
    verify(seriesRepository, times(1)).findAll(any(Sort.class));
    assertThat(findRecentSortList).isEqualTo(recentSeriesList);
  }

  @Test
  @DisplayName("시리즈를 인기순으로 조회할 수 있다.")
  void popularSort_findAllTest() {
    //given
    Long writerId = WRITER_ID;
    Sort popularSort = Sort.by(Direction.DESC, "likes");
    Comparator<Series> comparator = Comparator.comparingInt(s -> s.getWriter().getFollowCount());
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }

    List<Series> popularSeriesList = seriesList.stream()
      .sorted(comparator.reversed())
      .collect(Collectors.toList());
    doReturn(popularSeriesList)
      .when(this.seriesRepository)
      .findAll(any(Sort.class));

    //when
    List<Series> findPopularSortList = this.seriesService.findAll(popularSort);

    //then
    verify(seriesRepository, times(1)).findAll(any(Sort.class));
    assertThat(findPopularSortList).isEqualTo(popularSeriesList);
  }

  @Test
  @DisplayName("구독 상태로 시리즈 리스트를 조회할 수 있다.")
  void findBySubscribeStatusTest() {
    //given
    Long writerId = WRITER_ID;
    Pageable pageable = PageRequest.of(
      PAGE_NUM, SERIES_SIZE, Sort.by(Direction.DESC, "createdAt", "id")
    );
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 20; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    seriesList = seriesList.stream()
      .filter(s -> s.getSubscribeStatus().equals(SeriesStatus.SUBSCRIPTION_AVAILABLE))
      .sorted(Comparator
        .comparing(Series::getCreatedAt)
        .thenComparing(Series::getId)
        .reversed()
      )
      .limit(SERIES_SIZE)
      .collect(Collectors.toList());

    doReturn(seriesList)
      .when(seriesRepository)
      .findBySubscribeStatus(any(SeriesStatus.class), any(Pageable.class));

    //when
    List<Series> findSeries = this.seriesService.findBySubscribeStatus(
      SeriesStatus.SUBSCRIPTION_AVAILABLE, pageable
    );

    //then
    verify(this.seriesRepository, times(1))
      .findBySubscribeStatus(any(SeriesStatus.class), any(Pageable.class));
    assertThat(findSeries).isEqualTo(seriesList);
  }

  @Test
  @DisplayName("스케쥴러에 의해 상태값을 바꿔줄 수 있다.")
  void changeSeriesStatusTest() {
    //given
    Long writerId = WRITER_ID;
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    LocalDate today = LocalDate.now();
    seriesList = this.seriesRepository.findAll()
      .stream()
      .peek(series -> {
        series.changeSeriesStatus(today);
      })
      .collect(Collectors.toList());

    when(seriesRepository.saveAll(seriesList)).thenReturn(seriesList);

    //when
    this.seriesService.changeSeriesStatus();

    //then
    verify(seriesRepository, times(1)).saveAll(new ArrayList<>());
  }
}
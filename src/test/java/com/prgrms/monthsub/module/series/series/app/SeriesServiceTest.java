package com.prgrms.monthsub.module.series.series.app;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate.UploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class SeriesServiceTest {

  @Mock
  private SeriesRepositoryCustom seriesRepository;

  @Mock
  private ArticleUploadDateRepository articleUploadDateRepository;

  @InjectMocks
  private SeriesService seriesService;

  public Series getSeriesFixture(
    Long id,
    Long writerId
  ) {
    Series series = Mockito.mock(Series.class);
    Writer writer = getWriterFixture(writerId);
    given(series.getId()).willReturn(id);
    given(series.getTitle()).willReturn("타이틀" + id);
    given(series.getWriter()).willReturn(writer);
    given(series.getSubscribeStatus()).willReturn(SeriesStatus.SUBSCRIPTION_AVAILABLE);
    given(series.getCategory()).willReturn(Category.ESSAY);
    given(series.getPrice()).willReturn(1000);
    given(series.getArticleCount()).willReturn(10);
    given(series.getIntroduceSentence()).willReturn("소개 문장");
    given(series.getCreatedAt()).willReturn(LocalDateTime.now());
    return series;
  }

  public Writer getWriterFixture(Long id) {
    Writer writer = Mockito.mock(Writer.class);
    given(writer.getId()).willReturn(id);
    given(writer.getFollowCount()).willReturn((int) Math.pow(id, 2));
    return writer;
  }

  public ArticleUploadDate getArticleUploadDateFixture(
    Long id,
    Long seriesId
  ) {
    ArticleUploadDate articleUploadDate = Mockito.mock(ArticleUploadDate.class);
    given(articleUploadDate.getId()).willReturn(id);
    given(articleUploadDate.getSeriesId()).willReturn(seriesId);
    given(articleUploadDate.getUploadDate()).willReturn(UploadDate.FRIDAY);
    return articleUploadDate;
  }

  @Test
  @DisplayName("시리즈를 저장할 수 있다.")
  void saveTest() {

    //given
    Series series = getSeriesFixture(3L, 1L);
    doReturn(series).when(seriesRepository).save(any());

    //when
    Long id = seriesService.save(series);

    //then
    verify(seriesRepository, times(1)).save(any(Series.class));
    assertThat(id).isEqualTo(series.getId());
  }

  @Test
  @DisplayName("시리즈 단건 조회를 할 수 있다.")
  void getByIdTest() {

    //given
    Series series = getSeriesFixture(3L, 1L);
    doReturn(Optional.ofNullable(series)).when(seriesRepository).findById(any(Long.class));

    //when
    Series findSeries = seriesService.getById(3L);

    //then
    verify(seriesRepository, times(1)).findById(anyLong());
    assertThat(findSeries.getId()).isEqualTo(series.getId());
    assertThat(findSeries.getCategory()).isEqualTo(series.getCategory());
  }


  @Test
  @DisplayName("아티클 업로드 날짜를 저장할 수 있다.")
  void articleUploadDateSaveTest() {

    //given
    Series series = getSeriesFixture(3L, 1L);
    ArticleUploadDate articleUploadDate = getArticleUploadDateFixture(1L, series.getId());
    doNothing()
      .when(articleUploadDateRepository)
      .save(any(ArticleUploadDate.class));

    //when
    seriesService.articleUploadDateSave(articleUploadDate);

    //then
    verify(articleUploadDateRepository, times(1)).save(any(ArticleUploadDate.class));
  }

  @Test
  @DisplayName("아티클 업로드 날짜를 가져올 수 있다.")
  void getArticleUploadDateTest() {

    //given
    Series series = getSeriesFixture(3L, 1L);
    List<ArticleUploadDate> articleUploadDateList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      articleUploadDateList.add(getArticleUploadDateFixture(i, series.getId()));
    }
    doReturn(articleUploadDateList)
      .when(articleUploadDateRepository)
      .findAllBySeriesId(any(Long.class));

    //when
    List<ArticleUploadDate> findList = seriesService.getArticleUploadDate(series.getId());

    //then
    verify(articleUploadDateRepository, times(1))
      .findAllBySeriesId(anyLong());
    assertThat(findList.size()).isEqualTo(articleUploadDateList.size());
  }

  @Test
  @DisplayName("특정 작가의 `입력된 모집 상태`와 같은 공고가 있는지 확인 할 수 있다.")
  public void checkSeriesStatusByWriterIdTest() {

    //given
    Long writerId = 1L;
    SeriesStatus status = SeriesStatus.SUBSCRIPTION_AVAILABLE;
    Series series = getSeriesFixture(3L, writerId);
    doReturn(true)
      .when(seriesRepository)
      .existsAllByWriterIdAndSubscribeStatus(any(Long.class), any(SeriesStatus.class));

    //when
    boolean check = seriesService.checkSeriesStatusByWriterId(writerId, status);

    //then
    verify(seriesRepository, times(1))
      .existsAllByWriterIdAndSubscribeStatus(anyLong(), any(SeriesStatus.class));
    assertThat(check).isTrue();
    assertThat(series.getSubscribeStatus()).isEqualTo(status);
    assertThat(series.getWriter().getId()).isEqualTo(writerId);
  }

  @Test
  @DisplayName("특정 작가가 발행한 시리즈들을 조회 할 수 있다.")
  void findAllByWriterIdTest() {

    //given
    Long writerId = 1L;
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    doReturn(seriesList)
      .when(seriesRepository)
      .findAllByWriterId(any(Long.class));

    //when
    List<Series> findSeriesList = seriesService.findAllByWriterId(writerId);

    //then
    verify(seriesRepository, times(1)).findAllByWriterId(anyLong());
    assertThat(findSeriesList.size()).isEqualTo(seriesList.size());
    findSeriesList
      .forEach(series -> {
        assertThat(series.getWriter().getId()).isEqualTo(writerId);
      });
  }

  @Test
  @DisplayName("입력된 제목을 가진 시리즈를 조회 할 수 있다.")
  void getSeriesSearchTitle() {

    //given
    Long writerId = 1L;
    String title = "타이틀";
    List<Series> seriesList = new ArrayList<>();
    for (long i = 0; i < 10; i++) {
      seriesList.add(getSeriesFixture(i, writerId));
    }
    doReturn(seriesList)
      .when(seriesRepository)
      .findByTitleContainingIgnoreCase(anyString());

    //when
    List<Series> findSeriesList = seriesService.getSeriesSearchTitle(title);

    //then
    verify(seriesRepository, times(1)).findByTitleContainingIgnoreCase(anyString());
    assertThat(findSeriesList.size()).isEqualTo(seriesList.size());
    findSeriesList
      .forEach(series -> {
        assertThat(series.getTitle().contains(title)).isTrue();
      });
  }

  @Test
  @DisplayName("시리즈를 최신순으로 조회 할 수 있다.")
  void recentSort_findAllTest() {

    //given
    Long writerId = 1L;
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
      .when(seriesRepository)
      .findAll(any(Sort.class));

    //when
    List<Series> findRecentSortList = seriesService.findAll(recentSort);

    //then
    verify(seriesRepository, times(1)).findAll(any(Sort.class));
    for (int i = 0; i < 10; i++) {
      assertThat(findRecentSortList.get(i).getId()).isEqualTo(recentSeriesList.get(i).getId());
    }
  }

  @Test
  @DisplayName("시리즈를 인기순으로 조회 할 수 있다.")
  void popularSort_findAllTest() {

    //given
    Long writerId = 1L;
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
      .when(seriesRepository)
      .findAll(any(Sort.class));

    //when
    List<Series> findPopularSortList = seriesService.findAll(popularSort);

    //then
    verify(seriesRepository, times(1)).findAll(any(Sort.class));
    for (int i = 0; i < 10; i++) {
      assertThat(findPopularSortList.get(i).getId()).isEqualTo(popularSeriesList.get(i).getId());
    }
  }

}
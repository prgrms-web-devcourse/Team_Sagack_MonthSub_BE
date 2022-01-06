package com.prgrms.monthsub.module.series.article.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ArticleServiceTest {

  @InjectMocks
  private ArticleService articleService;

  @Mock
  private ArticleRepository articleRepository;

  private static final String FILE_KEY = "originalFilename.jpg";
  private Series series;
  private Article article;

  @BeforeEach
  void setUp() {
    this.series = Series.builder()
      .thumbnailKey(FILE_KEY)
      .title("제목")
      .introduceSentence("안녕하세요")
      .introduceText("안녕하세요")
      .price(10000)
      .subscribeStartDate(LocalDate.of(2021, 01, 22))
      .subscribeEndDate(LocalDate.of(2021, 02, 22))
      .seriesStartDate(LocalDate.of(2021, 03, 22))
      .subscribeEndDate(LocalDate.of(2021, 04, 22))
      .articleCount(10)
      .subscribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE)
      .likes(100)
      .category(Category.CRITIQUE)
      .uploadTime(LocalTime.now())
      .build();
    ReflectionTestUtils.setField(this.series, "id", 1L);

    this.article = Article.builder()
      .series(this.series)
      .title("제목")
      .contents("내용")
      .round(10)
      .build();
    ReflectionTestUtils.setField(this.article, "id", 1L);
  }

  @Test
  @DisplayName("아티클을 저장할 수 있다.")
  public void saveArticleTest() {
    //when
    when(this.articleRepository.save(any())).thenReturn(this.article);
    Long id = this.articleService.save(this.article);

    //then
    assertThat(this.article.getId(), equalTo(id));
    assertThat(this.article.getTitle(), equalTo("제목"));
    assertThat(this.article.getContents(), equalTo("내용"));
    assertThat(this.article.getSeries().getId(), equalTo(1L));
    assertThat(this.article.getSeries().getPrice(), equalTo(10000));
  }

  @Test
  @DisplayName("시리즈 아이디로 아티클을 조회할 수 있다.")
  public void findArticleTest() {
    //when
    when(this.articleRepository.findAllBySeriesId(1L)).thenReturn(List.of(this.article));
    List<Article> articles = this.articleService.getArticleListBySeriesId(1L);

    //then
    assertThat(articles.size(), equalTo(1));
  }

}

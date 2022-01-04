package com.prgrms.monthsub.module.series.article.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.domain.Series;
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

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ArticleServiceTest {

  @InjectMocks
  private ArticleService articleService;

  @Mock
  private ArticleRepository articleRepository;

  private Series series;
  private Article article;

  @BeforeEach
  void setUp() {
    this.series = Mockito.mock(Series.class);
    given(this.series.getId()).willReturn(1L);
    given(this.series.getPrice()).willReturn(10000);

    this.article = Mockito.mock(Article.class);
    given(this.article.getId()).willReturn(1L);
    given(this.article.getTitle()).willReturn("제목");
    given(this.article.getContents()).willReturn("내용");
    given(this.article.getRound()).willReturn(5);
    given(this.article.getSeries()).willReturn(series);
  }

  @Test
  @DisplayName("아티클을 저장할 수 있다.")
  public void saveArticleTest() {
    //given
    this.series = Mockito.mock(Series.class);
    given(this.series.getId()).willReturn(1L);
    given(this.series.getPrice()).willReturn(10000);

    this.article = Mockito.mock(Article.class);
    given(this.article.getId()).willReturn(1L);
    given(this.article.getTitle()).willReturn("제목");
    given(this.article.getContents()).willReturn("내용");
    given(this.article.getRound()).willReturn(5);
    given(this.article.getSeries()).willReturn(this.series);

    //when
    when(this.articleRepository.save(any())).thenReturn(this.article);
    Long id = this.articleService.save(article);

    //then
    assertThat(this.article.getId(), equalTo(id));
    assertThat(this.article.getTitle(), equalTo("제목"));
    assertThat(this.article.getContents(), equalTo("내용"));
    assertThat(this.article.getRound(), equalTo(5));
    assertThat(this.article.getSeries().getId(), equalTo(1L));
    assertThat(this.article.getSeries().getPrice(), equalTo(10000));
  }

  @Test
  @DisplayName("시리즈 아이디로 아티클을 조회할 수 있다.")
  public void findArticleTest() {
    //given
    this.series = Mockito.mock(Series.class);
    given(this.series.getId()).willReturn(1L);
    given(this.series.getPrice()).willReturn(10000);

    this.article = Mockito.mock(Article.class);
    given(this.article.getId()).willReturn(1L);
    given(this.article.getTitle()).willReturn("제목");
    given(this.article.getContents()).willReturn("내용");
    given(this.article.getRound()).willReturn(5);
    given(this.article.getSeries()).willReturn(this.series);

    //when
    when(this.articleRepository.findAllBySeriesId(1L)).thenReturn(List.of(this.article));
    List<Article> articles = this.articleService.getArticleListBySeriesId(1L);

    //then
    assertThat(articles.size(), equalTo(1));
  }

}

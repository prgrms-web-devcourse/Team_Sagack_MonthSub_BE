package com.prgrms.monthsub.module.series.article.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.app.SeriesService;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ArticleAssembleTest {

  @InjectMocks
  private ArticleAssemble articleAssemble;

  @Mock
  private ArticleService articleService;

  @Mock
  private SeriesService seriesService;

  @Mock
  private ArticleConverter articleConverter;

  @Mock
  private S3Client s3Client;

  private static final String FILE_KEY = "originalFilename.jpg";

  @Test
  @DisplayName("아티클을 생성할 수 있다.")
  public void createArticleTest() {
    MockMultipartFile file = new MockMultipartFile(
      "file",
      FILE_KEY,
      "multipart/form-data",
      "file".getBytes()
    );

    User user = User.builder()
      .email("dahee@test.com")
      .username("다희")
      .password("testpassword")
      .nickname("히히")
      .point(10000)
      .build();

    Writer writer = Writer.builder()
      .followCount(10)
      .user(user)
      .build();

    Series series = Series.builder()
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
      .writer(writer)
      .build();

    Article article = Article.builder()
      .series(series)
      .title("제목")
      .contents("내용")
      .round(10)
      .build();
    ReflectionTestUtils.setField(article, "id", 1L);

    ArticlePost.Request request = ArticlePost.Request.builder()
      .seriesId(1L)
      .title("제목")
      .contents("내용")
      .build();

    when(this.seriesService.getById(anyLong())).thenReturn(series);
    when(this.articleService.countBySeriesId(anyLong())).thenReturn(request.seriesId());
    when(this.articleConverter.toEntity(any(), any(), anyInt())).thenReturn(article);
    when(this.articleService.save(any())).thenReturn(1L);
    when(this.s3Client.getExtension(any())).thenReturn(FILE_KEY);
    when(this.s3Client.upload(any(), any(), any())).thenReturn(FILE_KEY);

    ArticlePost.Response response = this.articleAssemble.createArticle(file, request, user.getId());

    //Then
    verify(this.articleService, times(1)).save(any());

    assertThat(response.id(), equalTo(1L));
    assertThat(response.isMine(), equalTo(true));
  }

}

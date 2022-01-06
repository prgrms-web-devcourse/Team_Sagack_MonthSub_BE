package com.prgrms.monthsub.module.series.article.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.payment.bill.app.provider.PaymentProvider;
import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.domain.exception.ArticleException.ViewUnAuthorize;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne;
import com.prgrms.monthsub.module.series.article.dto.ArticleOne.Response;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.app.SeriesService;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
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
  private PaymentProvider paymentProvider;

  @Mock
  private S3Client s3Client;

  private static final String FILE_KEY = "originalFilename.jpg";
  private static final int ARTICLE_COUNT = 10;

  private User user;
  private Writer writer;
  private Series series;
  private Article article;
  private MockMultipartFile file;

  @BeforeEach
  void setUp() {
    this.user = User.builder()
      .email("dahee@test.com")
      .username("다희")
      .password("testpassword")
      .nickname("히히")
      .point(10000)
      .build();
    ReflectionTestUtils.setField(this.user, "id", 1L);

    this.writer = Writer.builder()
      .followCount(10)
      .user(user)
      .build();
    ReflectionTestUtils.setField(this.writer, "id", 1L);

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
      .articleCount(ARTICLE_COUNT)
      .subscribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE)
      .likes(100)
      .category(Category.CRITIQUE)
      .uploadTime(LocalTime.now())
      .writer(writer)
      .build();
    ReflectionTestUtils.setField(this.series, "id", 1L);

    this.article = Article.builder()
      .series(series)
      .title("제목")
      .contents("내용")
      .round(10)
      .build();
    ReflectionTestUtils.setField(this.article, "id", 1L);

    this.file = new MockMultipartFile(
      "file",
      FILE_KEY,
      "multipart/form-data",
      "file".getBytes()
    );
  }

  @Test
  @DisplayName("아티클을 생성할 수 있다.")
  public void createArticleTest() {
    //given
    ArticlePost.Request request = ArticlePost.Request.builder()
      .seriesId(1L)
      .title("제목")
      .contents("내용")
      .build();

    when(this.seriesService.getById(anyLong())).thenReturn(this.series);
    when(this.articleService.countBySeriesId(anyLong())).thenReturn(request.seriesId());
    when(this.articleConverter.toEntity(any(), any(), anyInt())).thenReturn(this.article);
    when(this.articleService.save(any())).thenReturn(1L);
    when(this.s3Client.getExtension(any())).thenReturn(FILE_KEY);
    when(this.s3Client.upload(any(), any(), any())).thenReturn(FILE_KEY);

    //when
    ArticlePost.Response response = this.articleAssemble.createArticle(
      this.file, request, this.user.getId()
    );

    //then
    verify(this.articleService, times(1)).save(any());

    assertThat(response.id(), equalTo(1L));
    assertThat(response.isMine(), equalTo(true));
  }

  @Test
  @DisplayName("본인이 만든 시리즈가 아니면 아티클을 생성할 수 없다.")
  public void createArticleAccessDeniedExceptionTest() {
    //given
    ArticlePost.Request request = ArticlePost.Request.builder()
      .seriesId(1L)
      .title("제목")
      .contents("내용")
      .build();

    //when
    when(this.seriesService.getById(anyLong())).thenReturn(this.series);
    when(this.articleService.countBySeriesId(anyLong())).thenReturn(request.seriesId());
    when(this.articleConverter.toEntity(any(), any(), anyInt())).thenReturn(this.article);

    assertThrows(
      AccessDeniedException.class,
      () -> this.articleAssemble.createArticle(this.file, request, 10L)
    );

    //then
    verify(this.articleService, never()).save(any());
  }

  @Test
  @DisplayName("내가 작성한 아티클을 단건 조회할 수 있다.")
  public void getArticleTest() {
    //given
    ArticleOne.Response response = Response.builder()
      .isMine(true)
      .title("제목")
      .contents("내용")
      .thumbnailKey(FILE_KEY)
      .round(10)
      .nickname("히히")
      .profileKey(FILE_KEY)
      .profileIntroduce("안녕하세요")
      .build();

    when(this.articleService.find(anyLong())).thenReturn(this.article);
    when(this.articleService.countBySeriesId(anyLong())).thenReturn(Long.valueOf(ARTICLE_COUNT));
    when(this.articleConverter.toArticleOneResponse(
      any(), any(), anyLong(), any())
    )
      .thenReturn(response);

    //when
    response = this.articleAssemble.getArticle(1L, 1L, this.user.getId());

    //then
    assertThat(response.isMine(), equalTo(true));
  }

  @Test
  @DisplayName("내가 구입한 아티클을 단건 조회할 수 있다.")
  public void getPurchasedArticleTest() {
    //given
    ArticleOne.Response response = Response.builder()
      .isMine(false)
      .title("제목")
      .contents("내용")
      .thumbnailKey(FILE_KEY)
      .round(10)
      .nickname("히히")
      .profileKey(FILE_KEY)
      .profileIntroduce("안녕하세요")
      .build();

    Payment payment = Payment.builder()
      .userId(10L)
      .series(this.series)
      .build();
    ReflectionTestUtils.setField(payment, "id", 1L);

    when(this.articleService.find(anyLong())).thenReturn(this.article);
    when(this.articleService.countBySeriesId(anyLong())).thenReturn(Long.valueOf(ARTICLE_COUNT));
    when(this.articleConverter.toArticleOneResponse(
      any(), any(), anyLong(), any())
    )
      .thenReturn(response);
    when(this.paymentProvider.find(anyLong(), anyLong())).thenReturn(Optional.of(payment));

    //when
    response = this.articleAssemble.getArticle(1L, 1L, 10L);

    //then
    assertThat(response.isMine(), equalTo(false));
  }

  @Test
  @DisplayName("구입하지 않으면 아티클을 조회할 수 없다.")
  public void getNotPurchasedArticleTest() {
    //given
    when(this.articleService.find(anyLong())).thenReturn(this.article);
    when(this.paymentProvider.find(anyLong(), anyLong())).thenReturn(Optional.empty());

    //when
    assertThrows(
      ViewUnAuthorize.class,
      () -> this.articleAssemble.getArticle(1L, 1L, 10L)
    );

    //then
    verify(this.articleConverter, never()).toArticleOneResponse(any(), any(), anyLong(), any());
  }

}

package com.prgrms.monthsub.module.payment.bill.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.payment.bill.converter.PaymentConverter;
import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import com.prgrms.monthsub.module.payment.bill.domain.Payment.State;
import com.prgrms.monthsub.module.payment.bill.domain.exception.PaymentException.PaymentDuplicated;
import com.prgrms.monthsub.module.payment.bill.dto.PaymentPost;
import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
  private static final String FILE_KEY = "originalFilename.jpg";
  private static final int ARTICLE_COUNT = 10;
  @Mock
  SeriesProvider seriesProvider;
  @Mock
  UserProvider userProvider;
  @Mock
  PaymentConverter paymentConverter;
  @Mock
  PaymentRepository paymentRepository;
  @InjectMocks
  private PaymentService paymentService;
  private Series series;
  private Writer writer;
  private Payment payment;
  private User user;
  private PaymentPost.Response response;

  @BeforeEach
  void setUp() {
    this.user = User.builder()
      .email("email")
      .username("userName")
      .password("user123")
      .nickname("nickName")
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

    this.payment = Payment.builder()
      .series(this.series)
      .userId(1L)
      .state(State.NULL)
      .build();

    this.response = PaymentPost.Response.builder()
      .email("email")
      .build();
  }

  @Test
  @DisplayName("결제를 할 수 있다.")
  public void createPaymentTest() {
    //given
    when(this.seriesProvider.getById(anyLong())).thenReturn(this.series);
    when(this.userProvider.findById(anyLong())).thenReturn(user);
    when(this.paymentRepository
      .findByUserIdAndSeriesIdAndState(anyLong(), anyLong(), any()))
      .thenReturn(
        Optional.empty()
      );
    when(this.paymentConverter.toPaymentPost(any(), any(), anyInt())).thenReturn(response);
    when(this.paymentConverter.toEntity(any(), any())).thenReturn(payment);
    when(this.paymentRepository.save(any())).thenReturn(payment);

    //when
    this.paymentService.pay(this.series.getId(), this.user.getId());

    //then
    assertThat(payment.getState(), is(State.PAY_COMPLETE));
    verify(paymentRepository, times(1)).save(any());
    verify(paymentRepository, times(1))
      .findByUserIdAndSeriesIdAndState(any(), any(), any());
  }

  @Test
  @DisplayName("중복 결제 시 예외가 발생한다.")
  public void duplicatedPayTest() {
    //given
    when(this.seriesProvider.getById(anyLong())).thenReturn(this.series);
    when(this.userProvider.findById(anyLong())).thenReturn(user);
    when(this.paymentRepository
      .findByUserIdAndSeriesIdAndState(anyLong(), anyLong(), any()))
      .thenReturn(
        Optional.of(payment)
      );
    when(this.paymentConverter.toPaymentPost(any(), any(), anyInt())).thenReturn(response);
    when(this.paymentRepository.save(any())).thenReturn(payment);

    //when,then
    Assertions.assertThrows(
      PaymentDuplicated.class,
      () -> this.paymentService.pay(this.series.getId(), this.user.getId())
    );
  }
}

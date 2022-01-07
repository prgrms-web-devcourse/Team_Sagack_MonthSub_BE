package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.WriterLikesService;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.payment.bill.app.provider.PaymentProvider;
import com.prgrms.monthsub.module.payment.bill.domain.Payment;
import com.prgrms.monthsub.module.series.series.converter.MyChannelConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MyChannelAssemble {

  private final UserService userService;
  private final WriterLikesService writerLikesService;
  private final SeriesService seriesService;
  private final WriterService writerService;
  private final MyChannelConverter myChannelConverter;
  private final SeriesLikesService seriesLikesService;
  private final PaymentProvider paymentProvider;

  public MyChannelAssemble(
    UserService userService,
    WriterLikesService writerLikesService,
    SeriesService seriesService,
    WriterService writerService,
    MyChannelConverter myChannelConverter,
    SeriesLikesService seriesLikesService,
    PaymentProvider paymentProvider
  ) {
    this.userService = userService;
    this.writerLikesService = writerLikesService;
    this.seriesService = seriesService;
    this.writerService = writerService;
    this.myChannelConverter = myChannelConverter;
    this.seriesLikesService = seriesLikesService;
    this.paymentProvider = paymentProvider;
  }

  public MyChannel.Response getMyChannel(Long userId) {
    //0. 좋아요한 시리즈 가져오기
    List<Long> likeSeriesList = this.seriesLikesService.findAllByUserId(userId);

    //1. 유저 객체 가져오기
    User userEntity = this.userService.findById(userId);

    //2. 내가 팔로잉한 작가 리스트 가져오기
    List<Writer> writerLikesList = this.writerLikesService
      .getFollowWriterList(userId, LikesStatus.Like)
      .stream()
      .map(this.getWriterLikesWriterFunction())
      .collect(Collectors.toList());

    //3. 내가 구독한 시리즈 리스트 가져오기
    List<Series> mySubscribeList = this.paymentProvider
      .findAllMySubscribeByUserId(userId)
      .stream()
      .map(Payment::getSeries)
      .peek(series -> {
        if (likeSeriesList.contains(series.getId())) {
          series.changeSeriesIsLiked(true);
        }
      })
      .collect(Collectors.toList());

    //4. (유저가 작가면) 내가 발행한 리스트 가져오기
    return this.writerService
      .findByUserIdOrEmpty(userId)
      .map(writer -> myChannelConverter.toResponse(
        true,
        true,
        userEntity,
        writer,
        writerLikesList,
        mySubscribeList,
        seriesService.findAllByWriterId(writer.getId())
          .stream().peek(series -> {
            if (likeSeriesList.contains(series.getId())) {
              series.changeSeriesIsLiked(true);
            }
          }).collect(Collectors.toList())
      ))
      .orElseGet(() -> myChannelConverter.toResponseWithoutWriter(
        true,
        true,
        userEntity,
        writerLikesList,
        mySubscribeList
      ));
  }

  public MyChannel.OtherResponse getOtherChannel(
    Long otherUserId,
    Optional<Long> userIdOrEmpty
  ) {
    List<Long> likeSeriesList = userIdOrEmpty.map(
        this.seriesLikesService::findAllByUserId
      )
      .orElse(
        Collections.emptyList()
      );

    List<Long> followWriterList =
      userIdOrEmpty.map(userId -> this.writerLikesService.getFollowWriterList(
          userId, LikesStatus.Like
        )
        .stream()
        .map(writerLikes -> writerLikes.getWriter().getUser().getId())
        .collect(
          Collectors.toList())).orElse(Collections.emptyList()
      );

    Boolean isMine = userIdOrEmpty.isPresent() && otherUserId.equals(userIdOrEmpty.get());

    //1. 유저 객체 가져오기
    User userEntity = this.userService.findById(otherUserId);

    //2. 내가 팔로잉한 작가 리스트 가져오기
    List<Writer> writerLikesList = this.writerLikesService
      .getFollowWriterList(otherUserId, LikesStatus.Like)
      .stream()
      .map(this.getWriterLikesWriterFunction())
      .collect(Collectors.toList());

    return this.writerService
      .findByUserIdOrEmpty(otherUserId)
      .map(writer ->
        myChannelConverter.toResponse(
          followWriterList.contains(otherUserId),
          isMine,
          userEntity,
          writer,
          writerLikesList,
          seriesService.findAllByWriterId(writer.getId())
            .stream()
            .peek(series -> {
                if (likeSeriesList.contains(series.getId())) {
                  series.changeSeriesIsLiked(true);
                }
              }
            ).collect(Collectors.toList())
        )
      )
      .orElseGet(
        () -> myChannelConverter.toResponseWithoutWriter(
          false, isMine, userEntity, writerLikesList)
      );
  }

  private Function<WriterLikes, Writer> getWriterLikesWriterFunction() {
    return writerLikes -> {
      boolean isSubscription = this.seriesService.checkSeriesStatusByWriterId(
        writerLikes.getId(), SeriesStatus.SUBSCRIPTION_AVAILABLE
      );

      writerLikes.getWriter().editSubScribeStatus(isSubscription);

      return writerLikes.getWriter();
    };
  }

}

package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.WriterLikesService;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.series.series.converter.MyChannelConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.SeriesUser;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import java.util.List;
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
  private final SeriesUserService seriesUserService;
  private final SeriesLikesService seriesLikesService;
  private final MyChannelConverter myChannelConverter;

  public MyChannelAssemble(
    UserService userService,
    WriterLikesService writerLikesService,
    SeriesService seriesService,
    WriterService writerService,
    SeriesUserService seriesUserService,
    SeriesLikesService seriesLikesService,
    MyChannelConverter myChannelConverter
  ) {
    this.userService = userService;
    this.writerLikesService = writerLikesService;
    this.seriesService = seriesService;
    this.writerService = writerService;
    this.seriesUserService = seriesUserService;
    this.seriesLikesService = seriesLikesService;
    this.myChannelConverter = myChannelConverter;
  }

  public MyChannel.Response getMyChannel(Long userId) {
    //1. 유저 객체 가져오기
    User userEntity = this.userService.findById(userId);

    //2. 내가 팔로잉한 작가 리스트 가져오기
    List<Writer> writerLikesList = this.writerLikesService
      .findAllByUserIdAndAndLikesStatus(userId, LikesStatus.Like)
      .stream()
      .map(writerLikes -> {
          if (this.seriesService.checkSeriesStatusByWriterId(
            writerLikes.getId(),
            SeriesStatus.SUBSCRIPTION_AVAILABLE
          )) {
            writerLikes.getWriter()
              .editSubScribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE);
          } else {
            writerLikes.getWriter()
              .editSubScribeStatus(SeriesStatus.SUBSCRIPTION_UNAVAILABLE);
          }
          return writerLikes.getWriter();
        }
      )
      .collect(Collectors.toList());

    //3. 내가 구독한 시리즈 리스트 가져오기
    List<Series> mySubscribeList = this.seriesUserService.findAllMySubscribeByUserId(userId)
      .stream()
      .map(SeriesUser::getSeries)
      .collect(Collectors.toList());

    //4. (유저가 작가면) 내가 발행한 리스트 가져오기
    return this.writerService.findWriterObjectByUserId(userId)
      .map(writer -> {
        return myChannelConverter.myChannelToResponse(
          userEntity,
          writer,
          writerLikesList,
          mySubscribeList,
          seriesService.findAllByWriterId(writer.getId())
        );
      })
      .orElseGet(() -> {
        return myChannelConverter.myChannelToResponseWithoutWriter(
          userEntity,
          writerLikesList,
          mySubscribeList
        );
      });
  }

  public MyChannel.OtherResponse getOtherChannel(Long userId) {
    //1. 유저 객체 가져오기
    User userEntity = this.userService.findById(userId);

    //2. 내가 팔로잉한 작가 리스트 가져오기
    List<Writer> writerLikesList = this.writerLikesService
      .findAllByUserIdAndAndLikesStatus(userId, LikesStatus.Like)
      .stream()
      .map(writerLikes -> {
          if (this.seriesService.checkSeriesStatusByWriterId(
            writerLikes.getId(),
            SeriesStatus.SUBSCRIPTION_AVAILABLE
          )) {
            writerLikes.getWriter()
              .editSubScribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE);
          } else {
            writerLikes.getWriter()
              .editSubScribeStatus(SeriesStatus.SUBSCRIPTION_UNAVAILABLE);
          }
          return writerLikes.getWriter();
        }
      )
      .collect(Collectors.toList());

    return this.writerService.findWriterObjectByUserId(userId)
      .map(writer -> {
        return myChannelConverter.otherChannelToResponse(
          userEntity,
          writer,
          writerLikesList,
          seriesService.findAllByWriterId(writer.getId())
        );
      })
      .orElseGet(() -> {
        return myChannelConverter.otherChannelToResponseWithoutWriter(userEntity, writerLikesList);
      });
  }

}

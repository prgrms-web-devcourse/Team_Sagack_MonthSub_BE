package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.WriterLikesService;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import com.prgrms.monthsub.module.series.series.converter.MyChannelConverter;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes;
import com.prgrms.monthsub.module.series.series.domain.SeriesUser;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    List<WriterLikes> writerLikesList = this.writerLikesService.findAllByUserId(
      userId, LikesStatus.Like);

    //2.5 팔로우한 작가들의 모집현황 status 가져오기
    for (WriterLikes followingWriter : writerLikesList) {
      Page<SeriesStatus> status = this.seriesService.checkSeriesStatusByWriterId(
        followingWriter.getId(), SeriesStatus.SUBSCRIPTION_AVAILABLE
        , PageRequest.of(0, 1));
      if (status.getTotalElements() > 0) {
        followingWriter.getWriter()
          .editSubScribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE);
      } else {
        followingWriter.getWriter()
          .editSubScribeStatus(SeriesStatus.SUBSCRIPTION_UNAVAILABLE);
      }
    }

    List<Writer> writerLikes = writerLikesList.stream()
      .map(WriterLikes::getWriter)
      .collect(Collectors.toList());

    //3. 내가 좋아요한 시리즈 리스트 가져오기
    List<Series> myLikesSeries = this.seriesLikesService.findAllMySeriesLikeByUserId(userId)
      .stream()
      .map(SeriesLikes::getSeries)
      .collect(Collectors.toList());

    //4. 내가 구돋한 시리즈 리스트 가져오기
    List<Series> mySubscribeList = this.seriesUserService.findAllMySubscribeByUserId(userId)
      .stream()
      .map(SeriesUser::getSeries)
      .collect(Collectors.toList());

    //5. (작가면?) 내가 발행한 리스트 가져오기
    return this.writerService.findWriterObjectByUserId(userId)
      .map(e -> {
        return myChannelConverter.myChannelToResponse(userEntity, e
          , writerLikes
          , myLikesSeries
          , mySubscribeList
          , seriesService.findAllByWriterId(e.getId()));
      })
      .orElseGet(() -> {
        return myChannelConverter.myChannelToResponseWithoutWriter(userEntity
          , writerLikes
          , myLikesSeries
          , mySubscribeList);
      });
  }

}

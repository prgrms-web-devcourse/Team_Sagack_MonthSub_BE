package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MyChannelConverter {

  private final int DEFAULT_LIST_SIZE = 10;
  private final int FOLLOW_COUNT_DEFAULT = 0;
  private final UserConverter userConverter;
  private final WriterConverter writerConverter;
  private final SeriesConverter seriesConverter;

  public MyChannelConverter(
    UserConverter userConverter,
    WriterConverter writerConverter,
    SeriesConverter seriesConverter
  ) {
    this.userConverter = userConverter;
    this.writerConverter = writerConverter;
    this.seriesConverter = seriesConverter;
  }

  public MyChannel.Response toResponse(
    Boolean isFollowed,
    Boolean isMine,
    User user,
    Writer writer,
    List<Writer> followingWriterList,
    List<Series> seriesSubscribeList,
    List<Series> seriesPostList
  ) {
    return MyChannel.Response.builder()
      .isFollowed(isFollowed)
      .isMine(isMine)
      .user(userConverter.toSeriesOneWithUser(user, Optional.of(writer.getId())))
      .followIngCount(followingWriterList.size())
      .followWriterList(followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .limit(DEFAULT_LIST_SIZE)
        .collect(Collectors.toList()))
      .subscribeList(seriesSubscribeList.stream()
        .map(seriesConverter::toMyChannelSubscribeObject)
        .collect(Collectors.toList()))
      .followCount(writer.getFollowCount())
      .seriesPostList(seriesPostList.stream()
        .map(seriesConverter::toMyChannelSeriesObject)
        .collect(Collectors.toList()))
      .build();
  }

  public MyChannel.Response toResponseWithoutWriter(
    Boolean isFollowed,
    Boolean isMine,
    User user,
    List<Writer> followingWriterList,
    List<Series> seriesSubscribeList
  ) {
    return MyChannel.Response.builder()
      .isFollowed(isFollowed)
      .isMine(isMine)
      .user(userConverter.toSeriesOneWithUser(user, Optional.empty()))
      .followIngCount(followingWriterList.size())
      .followWriterList(followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .limit(DEFAULT_LIST_SIZE)
        .collect(Collectors.toList()))
      .subscribeList(seriesSubscribeList.stream()
        .map(seriesConverter::toMyChannelSubscribeObject)
        .collect(Collectors.toList()))
      .followCount(FOLLOW_COUNT_DEFAULT)
      .seriesPostList(Collections.emptyList())
      .build();
  }

  public MyChannel.OtherResponse toResponse(
    Boolean isFollowed,
    Boolean isMine,
    User user,
    Writer writer,
    List<Writer> followingWriterList,
    List<Series> seriesPostList
  ) {
    return MyChannel.OtherResponse.builder()
      .isFollowed(isFollowed)
      .isMine(isMine)
      .user(userConverter.toSeriesOneWithUser(user, Optional.of(writer.getId())))
      .followIngCount(followingWriterList.size())
      .followWriterList(followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .limit(DEFAULT_LIST_SIZE)
        .collect(Collectors.toList()))
      .followCount(writer.getFollowCount())
      .seriesPostList(seriesPostList.stream()
        .map(seriesConverter::toMyChannelSeriesObject)
        .collect(Collectors.toList()))
      .build();
  }

  public MyChannel.OtherResponse toResponseWithoutWriter(
    Boolean isFollowed,
    Boolean isMine,
    User user,
    List<Writer> followingWriterList
  ) {
    return MyChannel.OtherResponse.builder()
      .isFollowed(isFollowed)
      .isMine(isMine)
      .user(userConverter.toSeriesOneWithUser(user, Optional.empty()))
      .followIngCount(followingWriterList.size())
      .followWriterList(followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .limit(DEFAULT_LIST_SIZE)
        .collect(Collectors.toList()))
      .followCount(FOLLOW_COUNT_DEFAULT)
      .seriesPostList(Collections.emptyList())
      .build();
  }

}

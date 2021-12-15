package com.prgrms.monthsub.module.series.series.converter;

import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.OtherResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class MyChannelConverter {

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
    User user,
    Writer writer,
    List<Writer> followingWriterList,
    List<Series> seriesSubscribeList,
    List<Series> seriesPostList
  ) {
    return new MyChannel.Response(
      userConverter.toSeriesOneWithUser(user),
      followingWriterList.size(),
      followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .collect(Collectors.toList()),
      seriesSubscribeList.stream()
        .map(seriesConverter::seriesToMyChannelSubscribeObject)
        .collect(Collectors.toList()),
      writer.getFollowCount(),
      seriesPostList.stream()
        .map(seriesConverter::seriesToMyChannelSeriesObject)
        .collect(Collectors.toList())
    );
  }

  public MyChannel.Response toResponseWithoutWriter(
    User user,
    List<Writer> followingWriterList,
    List<Series> seriesSubscribeList
  ) {
    return new MyChannel.Response(
      userConverter.toSeriesOneWithUser(user),
      followingWriterList.size(),
      followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .collect(Collectors.toList()),
      seriesSubscribeList.stream()
        .map(seriesConverter::seriesToMyChannelSubscribeObject)
        .collect(Collectors.toList()),
      0,
      Collections.emptyList()
    );
  }

  public MyChannel.OtherResponse toResponse(
    User user,
    Writer writer,
    List<Writer> followingWriterList,
    List<Series> seriesPostList
  ) {
    return new OtherResponse(
      userConverter.toSeriesOneWithUser(user),
      followingWriterList.size(),
      followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .collect(Collectors.toList()),
      writer.getFollowCount(),
      seriesPostList.stream()
        .map(seriesConverter::seriesToMyChannelSeriesObject)
        .collect(Collectors.toList())
    );
  }

  public MyChannel.OtherResponse toResponseWithoutWriter(
    User user,
    List<Writer> followingWriterList
  ) {
    return new MyChannel.OtherResponse(
      userConverter.toSeriesOneWithUser(user),
      followingWriterList.size(),
      followingWriterList.stream()
        .map(writerConverter::toMyChannelFollowWriterObject)
        .collect(Collectors.toList()),
      0,
      Collections.emptyList()
    );
  }

}

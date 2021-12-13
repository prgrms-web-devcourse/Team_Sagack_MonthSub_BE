package com.prgrms.monthsub.module.part.writer.converter;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.dto.MainPage;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import com.prgrms.monthsub.module.series.series.dto.MyChannel.MyChannelFollowWriterObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesOneWithWriterResponse;
import org.springframework.stereotype.Component;

@Component
public class WriterConverter {

  private final UserConverter userConverter;
  private final S3 s3;

  public WriterConverter(
    UserConverter userConverter,
    S3 s3
  ) {
    this.userConverter = userConverter;
    this.s3 = s3;
  }

  public SeriesOneWithWriterResponse writerToSeriesOneWithWriterResponse(Writer writer) {
    return new SeriesOneWithWriterResponse(
      writer.getId(),
      writer.getFollowCount(),
      this.userConverter.userToSeriesOneWithUserResponse(writer.getUser())
    );
  }

  public MyChannel.MyChannelFollowWriterObject writerToMyChannelFollowWriterObject(
    Writer writer
  ) {
    return MyChannelFollowWriterObject.builder()
      .userId(writer.getUser()
        .getId())
      .writerId(writer.getId())
      .nickname(writer.getUser()
        .getNickname())
      .profileImage(this.s3.getDomain() + "/" + writer.getUser()
        .getProfileKey())
      .subscribeStatus(String.valueOf(writer.getSubScribeStatus()))
      .build();
  }

  public MainPage.MainPageFollowWriterObject writerToMainPageFollowWriterObject(Writer writer) {
    return MainPage.MainPageFollowWriterObject.builder()
      .userId(writer.getUser()
        .getId())
      .writerId(writer.getId())
      .nickname(writer.getUser()
        .getNickname())
      .profileImage(this.s3.getDomain() + "/" + writer.getUser()
        .getProfileKey())
      .subscribeStatus(String.valueOf(writer.getSubScribeStatus()))
      .build();
  }

}

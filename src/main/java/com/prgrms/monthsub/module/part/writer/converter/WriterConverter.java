package com.prgrms.monthsub.module.part.writer.converter;

import com.prgrms.monthsub.config.S3;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
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
      .id(writer.getId())
      .profileImage(this.s3.getDomain() + "/" + writer.getUser()
        .getProfileKey())
      .seriesStatus(String.valueOf(writer.getSubScribeStatus()))
      .build();
  }

}

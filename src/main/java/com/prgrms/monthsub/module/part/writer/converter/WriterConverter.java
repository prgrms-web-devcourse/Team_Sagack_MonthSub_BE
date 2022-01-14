package com.prgrms.monthsub.module.part.writer.converter;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.dto.WriterLikesList.LikesResponse;
import com.prgrms.monthsub.module.part.writer.dto.WriterList.WriterResponse;
import com.prgrms.monthsub.module.series.series.dto.MyChannel;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList.SeriesWriterResponse;
import java.util.Optional;
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

  public SeriesWriterResponse toSeriesOneWithWriter(Writer writer) {
    return new SeriesWriterResponse(
      writer.getId(),
      writer.getFollowCount(),
      this.userConverter.toSeriesOneWithUser(writer.getUser(), Optional.of(writer.getId()))
    );
  }

  public MyChannel.FollowWriterObject toMyChannelFollowWriterObject(
    Writer writer
  ) {
    return MyChannel.FollowWriterObject.builder()
      .userId(writer.getUser().getId())
      .writerId(writer.getId())
      .nickname(writer.getUser().getNickname())
      .profileImage(
        writer.getUser().getProfileKey() == null
          ? null
          : this.s3.getDomain() + "/" + writer.getUser().getProfileKey()
      )
      .subscribeStatus(String.valueOf(writer.getSubScribeStatus()))
      .build();
  }

  public WriterResponse writerToWriterRes(Writer writer) {
    return WriterResponse.builder()
      .userId(writer.getUser().getId())
      .writerId(writer.getId())
      .nickname(writer.getUser().getNickname())
      .profileImage(writer.getUser().getProfileKey() == null ? null
        : this.s3.getDomain() + "/" + writer.getUser().getProfileKey())
      .subscribeStatus(String.valueOf(writer.getSubScribeStatus()))
      .build();
  }

  public LikesResponse toWriterLikesList(WriterLikes writerLikes) {
    Writer writer = writerLikes.getWriter();
    User user = writer.getUser();

    return LikesResponse.builder()
      .writerLikesId(writerLikes.getId())
      .writerId(writer.getId())
      .nickname(user.getNickname())
      .profileIntroduce(user.getProfileIntroduce())
      .profileImage(
        user.getProfileKey() == null
          ? null
          : this.s3.getDomain() + "/" + user.getProfileKey())
      .followCount(writer.getFollowCount())
      .build();
  }

}

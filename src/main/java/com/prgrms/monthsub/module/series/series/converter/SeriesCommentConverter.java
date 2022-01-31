package com.prgrms.monthsub.module.series.series.converter;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;

import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment.CommentStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.CommentMetaInfoObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.CommentObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.ReplyCommentObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.UserInfoObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SeriesCommentConverter {

  private final S3 s3;

  private final DateTimeFormatter commentCreatedTimeFormatter = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd HH:mm"
  );

  public SeriesCommentConverter(S3 s3) {
    this.s3 = s3;
  }

  public SeriesComment toEntity(
    Long userId,
    SeriesCommentPost.Request request
  ) {
    return SeriesComment.builder()
      .comment(request.comment())
      .userId(userId)
      .seriesId(request.seriesId())
      .parentId(request.parentId())
      .commentStatus(CommentStatus.CREATED)
      .build();
  }

  public SeriesCommentList.Response toResponse(
    Optional<Long> userIdOrEmpty,
    List<SeriesComment> comments,
    List<SeriesComment> replyComments,
    List<User> users
  ) {
    if (comments.isEmpty()) {
      return SeriesCommentList.Response.builder()
        .commentObjects(Collections.emptyList())
        .build();
    }

    Map<Long, User> usersInfo = users.stream()
      .collect(Collectors.toMap(User::getId, identity()));

    Map<Long, List<SeriesComment>> replyCommentsInfo = replyComments.stream()
      .collect(groupingBy(SeriesComment::getParentId));

    List<CommentObject> commentObjects = comments.stream().map(comment -> {
        User user = usersInfo.get(comment.getUserId());

        UserInfoObject commentUserInfoObject = UserInfoObject.builder()
          .userId(user.getId())
          .nickname(user.getNickname())
          .profileImage(
            user.getProfileKey() == null ? null : this.s3.getDomain() + "/" + user.getProfileKey()
          )
          .build();

        CommentMetaInfoObject commentMetaInfoObject = CommentMetaInfoObject.builder()
          .commentId(comment.getId())
          .comment(comment.getComment())
          .isMine(userIdOrEmpty.map(userId -> {
              return userId.equals(comment.getUserId());
            }
          ).orElse(false))
          .commentStatus(comment.getCommentStatus())
          .createdDateTime(commentCreatedTimeFormatter.format(comment.getCreatedAt()))
          .build();

        List<SeriesComment> replySeriesComment = replyCommentsInfo.getOrDefault(
          comment.getId(), Collections.emptyList()
        );
        List<ReplyCommentObject> replyCommentObject = replySeriesComment.stream()
          .map(
            replyComment -> {
              User replyCommentUser = usersInfo.get(replyComment.getUserId());
              UserInfoObject replyCommentUserInfoObject = UserInfoObject.builder()
                .userId(replyCommentUser.getId())
                .nickname(replyCommentUser.getNickname())
                .profileImage(replyCommentUser.getProfileKey() == null ? null
                  : this.s3.getDomain() + "/" + replyCommentUser.getProfileKey()
                )
                .build();

              CommentMetaInfoObject replyCommentMetaInfoObject = CommentMetaInfoObject.builder()
                .commentId(replyComment.getId())
                .comment(replyComment.getComment())
                .isMine(userIdOrEmpty.map(userId -> {
                    return userId.equals(replyComment.getUserId());
                  }
                ).orElse(false))
                .commentStatus(replyComment.getCommentStatus())
                .createdDateTime(commentCreatedTimeFormatter.format(replyComment.getCreatedAt()))
                .build();

              return ReplyCommentObject.builder()
                .userInfoObject(replyCommentUserInfoObject)
                .commentMetaInfoObject(replyCommentMetaInfoObject)
                .build();
            }
          ).collect(Collectors.toList());

        return CommentObject.builder()
          .userInfoObject(commentUserInfoObject)
          .commentMetaInfoObject(commentMetaInfoObject)
          .replyCommentObjects(replyCommentObject)
          .build();
      }
    ).collect(Collectors.toList());

    return SeriesCommentList.Response.builder()
      .commentObjects(commentObjects)
      .build();
  }

}

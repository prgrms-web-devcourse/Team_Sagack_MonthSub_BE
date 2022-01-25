package com.prgrms.monthsub.module.series.series.converter;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.groupingBy;

import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import com.prgrms.monthsub.module.series.series.domain.SeriesComment.CommentStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.CommentMetaInfoObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.CommentObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.ReplyCommentObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.UserInfoObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentPost;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SeriesCommentConverter {

  public SeriesCommentConverter() {
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
    Map<Long, User> usersInfo = users.stream()
      .collect(Collectors.toMap(User::getId, identity()));

    Map<Long, List<SeriesComment>> replyCommentsInfo = replyComments.stream()
      .collect(groupingBy(SeriesComment::getParentId));

    List<CommentObject> commentObjects = comments.stream().map(comment -> {
        User user = usersInfo.get(comment.getUserId());

        UserInfoObject commentUserInfoObject = UserInfoObject.builder()
          .nickname(user.getNickname())
          .profileImage(user.getProfileKey())
          .build();

        CommentMetaInfoObject commentMetaInfoObject = CommentMetaInfoObject.builder()
          .isMine(userIdOrEmpty.map(userId -> {
              return userId.equals(comment.getUserId());
            }
          ).orElse(false))
          .isDeleted(comment.getCommentStatus() == CommentStatus.DELETED)
          .updatedDate(comment.getUpdateAt().toLocalDate())
          .build();

        List<SeriesComment> replySeriesComment = replyCommentsInfo.get(comment.getId());
        List<ReplyCommentObject> replyCommentObject = replySeriesComment.stream()
          .map(
            replyComment -> {
              User replyCommentUser = usersInfo.get(replyComment.getUserId());
              UserInfoObject replyCommentUserInfoObject = UserInfoObject.builder()
                .nickname(replyCommentUser.getNickname())
                .profileImage(replyCommentUser.getProfileKey())
                .build();

              CommentMetaInfoObject replyCommentMetaInfoObject = CommentMetaInfoObject.builder()
                .isMine(userIdOrEmpty.map(userId -> {
                    return userId.equals(replyComment.getUserId());
                  }
                ).orElse(false))
                .isDeleted(replyComment.getCommentStatus() == CommentStatus.DELETED)
                .updatedDate(replyComment.getUpdateAt().toLocalDate())
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

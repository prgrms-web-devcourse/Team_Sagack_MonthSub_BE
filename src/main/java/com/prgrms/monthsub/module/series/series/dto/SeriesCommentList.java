package com.prgrms.monthsub.module.series.series.dto;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment.CommentStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.CommentMetaInfoObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.CommentObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.ReplyCommentObject;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.Response;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentList.UserInfoObject;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

public sealed interface SeriesCommentList permits Response, CommentObject, ReplyCommentObject,
  UserInfoObject, CommentMetaInfoObject {

  @Schema(name = "SeriesCommentList.Response")
  record Response(
    List<CommentObject> commentObjects
  ) implements SeriesCommentList {
    @Builder
    public Response {
    }
  }

  @Schema(name = "SeriesCommentList.CommentObject")
  record CommentObject(
    UserInfoObject userInfoObject,
    CommentMetaInfoObject commentMetaInfoObject,
    List<ReplyCommentObject> replyCommentObjects
  ) implements SeriesCommentList {
    @Builder
    public CommentObject {
    }
  }

  @Schema(name = "SeriesCommentList.ReplyCommentObject")
  record UserInfoObject(
    Long userId,
    String nickname,
    String profileImage
  ) implements SeriesCommentList {
    @Builder
    public UserInfoObject {
    }
  }

  @Schema(name = "SeriesCommentList.CommentMetaInfoObject")
  record CommentMetaInfoObject(
    Long commentId,
    String comment,
    boolean isMine,
    CommentStatus commentStatus,
    String createdDateTime
    ) implements SeriesCommentList {
    @Builder
    public CommentMetaInfoObject {
    }
  }

  @Schema(name = "SeriesCommentList.ReplyCommentObject")
  record ReplyCommentObject(
    UserInfoObject userInfoObject,
    CommentMetaInfoObject commentMetaInfoObject
  ) implements SeriesCommentList {
    @Builder
    public ReplyCommentObject {
    }
  }

}

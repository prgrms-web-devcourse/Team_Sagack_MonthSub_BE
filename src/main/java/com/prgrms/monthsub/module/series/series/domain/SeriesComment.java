package com.prgrms.monthsub.module.series.series.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import com.prgrms.monthsub.module.series.series.dto.SeriesCommentEdit;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "series_comment")
public class SeriesComment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "comment", columnDefinition = "VARCHAR(300)", nullable = false)
  private String comment;

  @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
  private Long userId;

  @Column(name = "series_id", columnDefinition = "BIGINT", nullable = false)
  private Long seriesId;

  @Column(name = "parent_id", columnDefinition = "BIGINT")
  private Long parentId;

  @Enumerated(EnumType.STRING)
  @Column(name = "comment_status", columnDefinition = "VARCHAR(50)", nullable = false)
  private CommentStatus commentStatus;

  @Builder
  private SeriesComment(
    String comment,
    Long userId,
    Long seriesId,
    Long parentId,
    CommentStatus commentStatus
  ) {
    this.comment = comment;
    this.userId = userId;
    this.seriesId = seriesId;
    this.parentId = parentId;
    this.commentStatus = commentStatus;
  }

  public void editComment(String comment) {
    this.comment = comment;
    this.commentStatus = CommentStatus.MODIFIED;
  }

  public Boolean isMine(Long userId){
    return Objects.equals(this.userId, userId);
  }

  public enum CommentStatus {
    CREATED,
    MODIFIED,
    DELETED;

    public static CommentStatus of(String commentStatus) {
      return CommentStatus.valueOf(commentStatus.toUpperCase());
    }
  }

}

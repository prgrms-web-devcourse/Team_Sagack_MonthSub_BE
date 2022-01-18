package com.prgrms.monthsub.module.series.series.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import com.prgrms.monthsub.module.part.user.domain.User;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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

  @Column(name = "contents", columnDefinition = "VARCHAR(300)", nullable = false)
  private String contents;

  @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
  private Long userId;

  @Column(name = "series_id", columnDefinition = "BIGINT", nullable = false)
  private Long seriesId;

  @Column(name = "parent_id", columnDefinition = "BIGINT")
  private Long parentId;

  @Column(name = "sequence", columnDefinition = "INT", nullable = false)
  private int sequence;

  @Enumerated(EnumType.STRING)
  @Column(name = "comment_status", columnDefinition = "VARCHAR(50)", nullable = false)
  private CommentStatus commentStatus;

  @Builder
  private SeriesComment(
    String contents,
    Long userId,
    Long seriesId,
    Long parentId,
    int sequence,
    CommentStatus commentStatus
  ) {
    this.contents = contents;
    this.userId = userId;
    this.seriesId = seriesId;
    this.parentId = parentId;
    this.sequence = sequence;
    this.commentStatus = commentStatus;
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

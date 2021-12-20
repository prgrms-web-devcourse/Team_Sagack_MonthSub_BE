package com.prgrms.monthsub.module.series.series.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "series_likes")
public class SeriesLikes extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "user_id", columnDefinition = "BIGINT", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "likes_status", columnDefinition = "VARCHAR(50)")
  private LikesStatus likesStatus;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "series_id", referencedColumnName = "id")
  private Series series;

  @Builder
  private SeriesLikes(
    Long userId,
    LikesStatus likesStatus,
    Series series
  ) {
    this.userId = userId;
    this.likesStatus = likesStatus;
    this.series = series;
  }

  public LikesStatus changeLikeStatus(LikesStatus likesStatus) {
    this.likesStatus = likesStatus;
    return this.likesStatus;
  }

  public enum LikesStatus {
    Like,
    Nothing;

    public static LikesStatus of(String likesStatus) {
      return LikesStatus.valueOf(likesStatus.toUpperCase());
    }
  }

}

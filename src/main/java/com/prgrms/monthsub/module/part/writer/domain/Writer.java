package com.prgrms.monthsub.module.part.writer.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "writer")
public class Writer extends BaseEntity {

  public static final int DEFAULT_FOLLOW_COUNT = 0;

  @Transient
  SeriesStatus subScribeStatus;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @PositiveOrZero
  @Column(name = "follow_count", columnDefinition = "INT")
  private int followCount;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User user;

  @Builder
  private Writer(
    int followCount,
    User user,
    SeriesStatus subScribeStatus
  ) {
    this.followCount = followCount;
    this.user = user;
    this.subScribeStatus = subScribeStatus;
  }

  public void editSubScribeStatus(SeriesStatus status) {
    this.subScribeStatus = status;
  }

}

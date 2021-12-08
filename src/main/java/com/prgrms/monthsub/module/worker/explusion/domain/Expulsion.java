package com.prgrms.monthsub.module.worker.explusion.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "expulsion")
public class Expulsion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "user_id", columnDefinition = "BIGINT")
  private Long userId;

  @Column(name = "image_key", columnDefinition = "TEXT", nullable = false)
  private String imageKey;

  @Enumerated(EnumType.STRING)
  @Column(name = "expulsion_image_status", columnDefinition = "VARCHAR(50)", nullable = false)
  private ExpulsionImageStatus expulsionImageStatus;

  @Enumerated(EnumType.STRING)
  @Column(name = "expulsion_image_name", columnDefinition = "VARCHAR(50)", nullable = false)
  private ExpulsionImageName expulsionImageName;

  @Column(name = "hard_delete_date", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime hardDeleteDate;

  public enum ExpulsionImageName {

    SERIES_THUMBNAIL,
    ARTICLE_THUMBNAIL,
    USER_PROFILE;

    public static ExpulsionImageName of(String expulsionImageName) {
      return ExpulsionImageName.valueOf(expulsionImageName.toUpperCase());
    }
  }

  public enum ExpulsionImageStatus {

    CREATED,
    DELETED;

    public static ExpulsionImageStatus of(String imageExpulsionStatus) {
      return ExpulsionImageStatus.valueOf(imageExpulsionStatus.toUpperCase());
    }

  }

}

package com.prgrms.monthsub.module.worker.expulsion.domain;

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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "expulsion")
public class Expulsion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "domain_id", columnDefinition = "BIGINT", nullable = false)
  private Long domainId;

  @Column(name = "user_id", columnDefinition = "BIGINT")
  private Long userId;

  @Column(name = "file_key", columnDefinition = "TEXT", nullable = false)
  private String fileKey;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", columnDefinition = "VARCHAR(50)", nullable = false)
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(name = "domain_type", columnDefinition = "VARCHAR(50)", nullable = false)
  private DomainType domainType;

  @Enumerated(EnumType.STRING)
  @Column(name = "file_category", columnDefinition = "VARCHAR(50)", nullable = false)
  private FileCategory fileCategory;

  @Enumerated(EnumType.STRING)
  @Column(name = "file_type", columnDefinition = "VARCHAR(50)", nullable = false)
  private FileType fileType;

  @Column(name = "soft_delete_date", columnDefinition = "TIMESTAMP", updatable = false)
  private LocalDateTime softDeleteDate;

  @Column(name = "hard_delete_date", columnDefinition = "TIMESTAMP")
  private LocalDateTime hardDeleteDate;

  @Builder
  private Expulsion(
    Long domainId,
    Long userId,
    String fileKey,
    Status status,
    DomainType domainType,
    FileCategory fileCategory,
    FileType fileType
  ) {
    this.domainId = domainId;
    this.userId = userId;
    this.fileKey = fileKey;
    this.status = status;
    this.domainType = domainType;
    this.fileCategory = fileCategory;
    this.fileType = fileType;
    this.softDeleteDate = LocalDateTime.now();
  }

  public Expulsion hardDelete() {
    this.hardDeleteDate = LocalDateTime.now();
    this.status = Status.DELETED;
    return this;
  }

  public enum Status {
    CREATED,
    DELETED;

    public static Status of(String status) {
      return Status.valueOf(status.toUpperCase());
    }
  }

  public enum DomainType {
    USER,
    WRITER,
    SERIES,
    ARTICLE,
    BILL;
  }

  public enum FileCategory {
    SERIES_THUMBNAIL,
    ARTICLE_THUMBNAIL,
    USER_PROFILE;

    public static FileCategory of(String fileCategory) {
      return FileCategory.valueOf(fileCategory.toUpperCase());
    }
  }

  public enum FileType {
    TEXT,
    IMAGE,
    VIDEO;

    public static FileType of(String fileType) {
      return FileType.valueOf(fileType.toUpperCase());
    }
  }

}
package com.prgrms.monthsub.module.series.series.domain;

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
@Table(name = "article_upload_date")
public class ArticleUploadDate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "series_id", columnDefinition = "BIGINT", nullable = false)
  private Long seriesId;

  @Enumerated(EnumType.STRING)
  @Column(name = "date", columnDefinition = "VARCHAR(50)", nullable = false)
  private UploadDate uploadDate;

  @Builder
  private ArticleUploadDate(
    Long seriesId,
    UploadDate uploadDate
  ) {
    this.seriesId = seriesId;
    this.uploadDate = uploadDate;
  }

  public enum UploadDate {

    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    public static UploadDate of(String uploadDate) {
      return UploadDate.valueOf(uploadDate.toUpperCase());
    }
  }

}

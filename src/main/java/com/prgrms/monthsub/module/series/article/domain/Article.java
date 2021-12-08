package com.prgrms.monthsub.module.series.article.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import com.prgrms.monthsub.module.series.series.domain.Series;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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
@Table(name = "article")
public class Article extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "series_id", referencedColumnName = "id")
  private Series series;

  @Column(name = "title", columnDefinition = "VARCHAR(300)", nullable = false)
  private String title;

  @Column(name = "content", columnDefinition = "TEXT", nullable = false)
  private String contents;

  @Column(name = "thumbnail_key", columnDefinition = "TEXT")
  private String thumbnailKey;

  @Column(name = "round", columnDefinition = "INT", nullable = false)
  private int round;

  public void changeThumbnailKey(String thumbnailKey) {
    this.thumbnailKey = thumbnailKey;
  }

  public void changeWriting(
    String title,
    String contents
  ) {
    this.title = title;
    this.contents = contents;
  }

}

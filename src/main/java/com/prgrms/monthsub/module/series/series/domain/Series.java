package com.prgrms.monthsub.module.series.series.domain;

import com.prgrms.monthsub.common.domain.BaseEntity;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.series.series.domain.SeriesLikes.LikesStatus;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeEdit;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
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
@Table(name = "series")
public class Series extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", columnDefinition = "BIGINT")
  private Long id;

  @Column(name = "thumbnail_key", columnDefinition = "TEXT")
  private String thumbnailKey;

  @Column(name = "title", columnDefinition = "VARCHAR(300)", nullable = false)
  private String title;

  @Column(name = "introduce_text", columnDefinition = "TEXT", nullable = false)
  private String introduceText;

  @Column(name = "introduce_sentence", columnDefinition = "VARCHAR(300)", nullable = false)
  private String introduceSentence;

  @PositiveOrZero
  @Column(name = "price", columnDefinition = "INT", nullable = false)
  private int price;

  @Column(name = "subscribe_start_date", updatable = false, nullable = false)
  private LocalDate subscribeStartDate;

  @Column(name = "subscribe_end_date", updatable = false, nullable = false)
  private LocalDate subscribeEndDate;

  @Column(name = "series_start_date", updatable = false, nullable = false)
  private LocalDate seriesStartDate;

  @Column(name = "series_end_date", updatable = false, nullable = false)
  private LocalDate seriesEndDate;

  @PositiveOrZero
  @Column(name = "article_count", columnDefinition = "INT", nullable = false)
  private int articleCount;

  @Enumerated(EnumType.STRING)
  @Column(name = "subscribe_status", columnDefinition = "VARCHAR(50)", nullable = false)
  private SeriesStatus subscribeStatus;

  @PositiveOrZero
  @Column(name = "likes", columnDefinition = "INT")
  private int likes;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", columnDefinition = "VARCHAR(50)", nullable = false)
  private Category category;

  @Column(name = "upload_time", nullable = false)
  private LocalTime uploadTime;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_id", referencedColumnName = "id", nullable = false)
  private Writer writer;

  @Transient
  private boolean likedStatus = false;

  @Builder
  private Series(
    String thumbnailKey,
    String title,
    String introduceText,
    String introduceSentence,
    int price,
    LocalDate subscribeStartDate,
    LocalDate subscribeEndDate,
    LocalDate seriesStartDate,
    LocalDate seriesEndDate,
    int articleCount,
    SeriesStatus subscribeStatus,
    int likes,
    Category category,
    LocalTime uploadTime,
    Writer writer
  ) {
    this.thumbnailKey = thumbnailKey;
    this.title = title;
    this.introduceText = introduceText;
    this.introduceSentence = introduceSentence;
    this.price = price;
    this.subscribeStartDate = subscribeStartDate;
    this.subscribeEndDate = subscribeEndDate;
    this.seriesStartDate = seriesStartDate;
    this.seriesEndDate = seriesEndDate;
    this.articleCount = articleCount;
    this.subscribeStatus = subscribeStatus;
    this.likes = likes;
    this.category = category;
    this.uploadTime = uploadTime;
    this.writer = writer;
  }

  public void changeSeriesStatus(
    LocalDate today
  ) {
    if (this.subscribeStartDate.isBefore(today)
      && this.subscribeEndDate.isAfter(today)) {
      this.subscribeStatus = SeriesStatus.SUBSCRIPTION_AVAILABLE;
    } else if (this.seriesStartDate.isBefore(today)
      && this.seriesEndDate.isAfter(today)) {
      this.subscribeStatus = SeriesStatus.SERIALIZATION_AVAILABLE;
    } else {
      this.subscribeStatus = SeriesStatus.SUBSCRIPTION_AVAILABLE;
    }
  }

  public void editSeries(
    SeriesSubscribeEdit.Request request
  ) {
    this.title = request.title();
    this.introduceSentence = request.introduceSentence();
    this.introduceText = request.introduceText();
    this.uploadTime = LocalTime.parse(request.uploadTime());
  }

  public void changeLikesCount(LikesStatus changeStatus) {
    this.likes += changeStatus.equals(LikesStatus.Like) ? 1 : -1;
  }

  public void changeSeriesIsLiked(boolean isLiked) {
    this.likedStatus = isLiked;
  }

  public void changeThumbnailKey(String thumbnailKey) {
    this.thumbnailKey = thumbnailKey;
  }

  public Boolean isMine(Long userId) {
    return Objects.equals(this.getWriter()
      .getUser()
      .getId(), userId);
  }

  public enum Category {
    POEM,
    NOVEL,
    INTERVIEW,
    ESSAY,
    CRITIQUE,
    ETC;

    public static Category of(String category) {
      return Category.valueOf(category.toUpperCase());
    }

    public static List<Category> getCategories() {
      return List.of(
        Category.POEM,
        Category.NOVEL,
        Category.INTERVIEW,
        Category.ESSAY,
        Category.CRITIQUE,
        Category.ETC
      );
    }
  }

  public enum SeriesStatus {
    SERIALIZATION_AVAILABLE,
    SUBSCRIPTION_UNAVAILABLE,
    SUBSCRIPTION_AVAILABLE;

    public static SeriesStatus of(String seriesStatus) {
      return SeriesStatus.valueOf(seriesStatus.toUpperCase());
    }

    public static List<SeriesStatus> getAllStatus() {
      return List.of(
        SeriesStatus.SERIALIZATION_AVAILABLE,
        SeriesStatus.SUBSCRIPTION_UNAVAILABLE,
        SeriesStatus.SUBSCRIPTION_AVAILABLE
      );
    }
  }

}

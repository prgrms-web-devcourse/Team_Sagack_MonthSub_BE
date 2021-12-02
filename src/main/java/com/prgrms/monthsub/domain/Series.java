package com.prgrms.monthsub.domain;

import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.domain.enumType.SeriesStatus;
import java.time.LocalDateTime;
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
import javax.validation.constraints.Min;
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
@Table(name = "series")
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @Column(name = "thumbnail", columnDefinition = "TEXT", nullable = false)
    private String thumbnail;

    @Column(name = "title", columnDefinition = "VARCHAR(300)", nullable = false)
    private String title;

    @Column(name = "introduce_text", columnDefinition = "TEXT", nullable = false)
    private String introduceText;

    @Column(name = "introduce_sentence", columnDefinition = "VARCHAR(300)", nullable = false)
    private String introduceSentence;

    @Min(0)
    @Column(name = "price", columnDefinition = "INT", nullable = false)
    private int price;

    @Column(name = "subscribe_start_date", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private LocalDateTime subscribeStartDate;

    @Column(name = "subscribe_end_date", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private LocalDateTime subscribeEndDate;

    @Column(name = "series_start_date", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private LocalDateTime seriesStartDate;

    @Column(name = "series_end_date", columnDefinition = "TIMESTAMP", updatable = false, nullable = false)
    private LocalDateTime seriesEndDate;

    @Column(name = "article_count", columnDefinition = "INT", nullable = false)
    private int articleCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "subscribe_status", columnDefinition = "VARCHAR(50)", nullable = false)
    private SeriesStatus subscribeStatus;

    @Min(0)
    @Column(name = "likes", columnDefinition = "INT")
    private int likes;

    @Column(name = "category", columnDefinition = "VARCHAR(50)", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", referencedColumnName = "id")
    private Writer writer;

    @Column(name = "upload_date", columnDefinition = "VARCHAR(50)", nullable = false)
    private String uploadDate;

    @Column(name = "upload_time", columnDefinition = "time", nullable = false)
    private LocalDateTime uploadTime;

}

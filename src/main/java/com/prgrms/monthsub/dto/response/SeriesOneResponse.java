package com.prgrms.monthsub.dto.response;

import com.prgrms.monthsub.domain.enumType.Category;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.Builder;

public record SeriesOneResponse(
    SeriesObject series,
    UploadObject upload,
    SubscribeObject subscribe,
    Category category,
    WriterObject writer,
    List<BriefArticleBySeriesIdResponse> articleList
) {

    @Builder
    public static class SeriesObject {

        private Long id;

        private String thumbnail;

        private String title;

        private String introduceText;

        private String introduceSentence;

        private int price;

        private LocalDate startDate;

        private LocalDate endDate;

        private int articleCount;

        private int likes;

    }

    @Builder
    public static class UploadObject {

        private String[] date;

        private LocalTime time;

    }

    @Builder
    public static class SubscribeObject {

        private LocalDate startDate;

        private LocalDate endDate;

        private String status;

    }

    @Builder
    public static class WriterObject {

        private Long id;

        private Long userId;

        private int followCount;

        private String email;

        private String profileImage;

        private String profileIntroduce;

        private String nickname;

    }

}
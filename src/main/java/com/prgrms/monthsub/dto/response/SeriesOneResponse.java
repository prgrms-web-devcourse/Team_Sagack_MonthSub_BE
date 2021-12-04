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

        public Long id;

        public String thumbnail;

        public String title;

        public String introduceText;

        public String introduceSentence;

        public int price;

        public LocalDate startDate;

        public LocalDate endDate;

        public int articleCount;

        public int likes;

    }

    @Builder
    public static class UploadObject {

        public String[] date;

        public LocalTime time;

    }

    @Builder
    public static class SubscribeObject {

        public LocalDate startDate;

        public LocalDate endDate;

        public String status;

    }

    @Builder
    public static class WriterObject {

        public Long id;

        public Long userId;

        public int followCount;

        public String email;

        public String profileImage;

        public String profileIntroduce;

        public String nickname;

    }

}
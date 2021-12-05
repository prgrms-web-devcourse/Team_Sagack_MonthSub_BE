package com.prgrms.monthsub.dto;

import com.prgrms.monthsub.domain.enumType.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Builder;

public class SeriesSubscribeList {

    @Schema(name = "SeriesSubscribeList")
    public record Response(
        SeriesObject series,
        SubscribeObject subscribe,
        Category category,
        WriterObject writer
    ) {

    }

    @Schema(name = "SeriesSubscribeList")
    public record SeriesOneWithUserResponse(
        Long userId,
        String email,
        String profileImage,
        String profileIntroduce,
        String nickname
    ) {
    }

    @Schema(name = "SeriesSubScribeList")
    public record SeriesOneWithWriterResponse(
        Long writerId,
        int followCount,
        SeriesOneWithUserResponse user
    ) {
    }

    @Schema(name = "SeriesSubScribeList")
    public record BriefArticleBySeriesIdResponse(
        Long articleId,
        String title,
        String contents,
        Integer round,
        LocalDate date
    ) {
    }

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



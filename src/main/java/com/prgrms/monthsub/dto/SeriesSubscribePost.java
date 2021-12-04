package com.prgrms.monthsub.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class SeriesSubscribePost {

    public record Request(

        @NotBlank(message = "닉네임이 비어있습니다.")
        String nickname,

        @NotBlank(message = "제목이 비어있습니다.")
        String title,

        @NotBlank(message = "닉네임이 비어있습니다.")
        String introduceSentence,

        @NotBlank(message = "소개문장이 비어있습니다.")
        String introduceText,

        @NotBlank(message = "구독 시작 날짜가 비어있습니다.")
        String subscribeStartDate,

        @NotBlank(message = "구독 종료 날짜가 비어있습니다.")
        String subscribeEndDate,

        @NotBlank(message = "연재 시작 날짜가 비어있습니다.")
        String seriesStartDate,

        @NotBlank(message = "연재 종료 날짜가 비어있습니다.")
        String seriesEndDate,

        @NotBlank(message = "카테고리가 비어있습니다.")
        String category,

        String[] uploadDate,

        @NotBlank(message = "업로드 시간이 비어있습니다.")
        String uploadTime,

        @Positive
        int articleCount,

        @Positive
        int price
    ) {
    }

    public record Response(
        Long seriesId
    ) {
    }

}

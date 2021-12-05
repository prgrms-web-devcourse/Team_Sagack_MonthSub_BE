package com.prgrms.monthsub.dto;

import javax.validation.constraints.NotBlank;

public class SeriesSubscribeEdit {

    public record Request(

        Long writerId,

        @NotBlank(message = "제목이 비어있습니다.")
        String title,

        @NotBlank(message = "시리즈 소개문장이 비어있습니다.")
        String introduceSentence,

        @NotBlank(message = "시리즈 글이 비어있습니다.")
        String introduceText,

        String[] uploadDate,

        @NotBlank(message = "업로드 시간이 비어있습니다.")
        String uploadTime
    ) {
    }

    public record Response(
        Long seriesId
    ) {
    }

}

package com.prgrms.monthsub.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

public class SeriesSubscribeEdit {

    @Schema(name = "SeriesSubscribeEdit")
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

    @Schema(name = "SeriesSubscribeEdit")
    public record Response(
        Long seriesId
    ) {
    }

}

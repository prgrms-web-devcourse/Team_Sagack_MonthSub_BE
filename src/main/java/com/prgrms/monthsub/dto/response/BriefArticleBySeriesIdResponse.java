package com.prgrms.monthsub.dto.response;

public record BriefArticleBySeriesIdResponse(
    Long articleId,
    String title,
    String contents,
    Integer round
) {
}
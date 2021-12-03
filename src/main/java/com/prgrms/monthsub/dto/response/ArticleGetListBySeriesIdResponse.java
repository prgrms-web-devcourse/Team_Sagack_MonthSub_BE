package com.prgrms.monthsub.dto.response;

public record ArticleGetListBySeriesIdResponse(
    Long articleId,
    String title,
    String contents,
    Integer round
) {
}
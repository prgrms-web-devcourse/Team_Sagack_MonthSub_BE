package com.prgrms.monthsub.converter;

import static com.prgrms.monthsub.common.utils.TimeUtil.convertUploadDateListToUploadDateString;

import com.prgrms.monthsub.domain.Article;
import com.prgrms.monthsub.domain.Series;
import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.domain.enumType.SeriesStatus;
import com.prgrms.monthsub.dto.request.SeriesSubscribePostRequest;
import com.prgrms.monthsub.dto.response.SeriesSubscribeGetResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class SeriesConverter {

    private static final int DEFAULT_LIKES = 0;

    private final ArticleConverter articleConverter;

    private final WriterConverter writerConverter;

    public SeriesConverter(ArticleConverter articleConverter,
        WriterConverter writerConverter) {
        this.articleConverter = articleConverter;
        this.writerConverter = writerConverter;
    }

    public Series SeriesSubscribePostResponseToEntity(Writer writer, String imageUrl,
        SeriesSubscribePostRequest req) {
        return Series.builder()
            .thumbnail(imageUrl)
            .title(req.title())
            .introduceText(req.introduceText())
            .introduceSentence(req.introduceSentence())
            .price(req.price())
            .subscribeStartDate(LocalDate.parse(req.subscribeStartDate()))
            .subscribeEndDate(LocalDate.parse(req.subscribeEndDate()))
            .seriesStartDate(LocalDate.parse(req.seriesStartDate()))
            .seriesEndDate(LocalDate.parse(req.seriesEndDate()))
            .articleCount(req.articleCount())
            .subscribeStatus(SeriesStatus.SUBSCRIPTION_AVAILABLE)
            .likes(DEFAULT_LIKES)
            .uploadDate(convertUploadDateListToUploadDateString(req.uploadDate()))
            .category(Category.of(req.category()))
            .uploadTime(LocalTime.parse(req.uploadTime()))
            .writer(writer)
            .build();
    }

    public SeriesSubscribeGetResponse seriesToSeriesSubscribeGetResponse(Series series,
        List<Article> articleList) {
        return new SeriesSubscribeGetResponse(
            series.getId(),
            series.getThumbnail(),
            series.getTitle(),
            series.getIntroduceText(),
            series.getIntroduceSentence(),
            series.getPrice(),
            series.getSubscribeStartDate(),
            series.getSubscribeEndDate(),
            series.getSeriesStartDate(),
            series.getSeriesEndDate(),
            series.getArticleCount(),
            series.getSubscribeStatus(),
            series.getLikes(),
            series.getCategory(),
            series.getUploadDate(),
            series.getUploadTime(),
            writerConverter.writerToResponse(series.getWriter()),
            articleList.stream()
                .map(articleConverter::articleToArticleGetListBySeriesIdResponse)
                .collect(Collectors.toList())
        );
    }

}

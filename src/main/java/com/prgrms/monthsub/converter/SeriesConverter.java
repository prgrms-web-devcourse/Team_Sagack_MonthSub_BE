package com.prgrms.monthsub.converter;

import static com.prgrms.monthsub.common.utils.TimeUtil.convertUploadDateListToUploadDateString;

import com.prgrms.monthsub.domain.Article;
import com.prgrms.monthsub.domain.Series;
import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.domain.enumType.SeriesStatus;
import com.prgrms.monthsub.dto.request.SeriesSubscribePostRequest;
import com.prgrms.monthsub.dto.response.SeriesListResponse;
import com.prgrms.monthsub.dto.response.SeriesOneResponse;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.SeriesObject;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.SubscribeObject;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.UploadObject;
import com.prgrms.monthsub.dto.response.SeriesOneResponse.WriterObject;
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

    public SeriesConverter(ArticleConverter articleConverter, WriterConverter writerConverter) {
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

    public SeriesOneResponse seriesToSeriesOneResponse(Series seriesEntity,
        List<Article> articleList) {
        var writerResponse = writerConverter.writerToSeriesOneWithWriterResponse(
            seriesEntity.getWriter());
        return new SeriesOneResponse(
            SeriesObject.builder()
                .id(seriesEntity.getId())
                .thumbnail(seriesEntity.getThumbnail())
                .title(seriesEntity.getTitle())
                .introduceText(seriesEntity.getIntroduceText())
                .introduceSentence(seriesEntity.getIntroduceSentence())
                .price(seriesEntity.getPrice())
                .startDate(seriesEntity.getSeriesStartDate())
                .endDate(seriesEntity.getSeriesEndDate())
                .articleCount(seriesEntity.getArticleCount())
                .likes(seriesEntity.getLikes())
                .build(),
            UploadObject.builder()
                .date(seriesEntity.getUploadDate().split("\\$"))
                .time(seriesEntity.getUploadTime())
                .build(),
            SubscribeObject.builder()
                .startDate(seriesEntity.getSubscribeStartDate())
                .endDate(seriesEntity.getSubscribeEndDate())
                .status(String.valueOf(seriesEntity.getSubscribeStatus()))
                .build(),
            seriesEntity.getCategory(),
            WriterObject.builder()
                .id(writerResponse.writerId())
                .userId(writerResponse.user().userId())
                .followCount(writerResponse.followCount())
                .email(writerResponse.user().email())
                .profileImage(writerResponse.user().profileImage())
                .profileIntroduce(writerResponse.user().profileIntroduce())
                .nickname(writerResponse.user().nickname())
                .build(),
            articleList.stream()
                .map(articleConverter::articleToArticleBySeriesIdResponse)
                .collect(Collectors.toList())
        );
    }

    public SeriesListResponse seriesListToResponse(Series seriesEntity) {
        var writerResponse = writerConverter.writerToSeriesOneWithWriterResponse(
            seriesEntity.getWriter());
        return new SeriesListResponse(
            SeriesObject.builder()
                .id(seriesEntity.getId())
                .thumbnail(seriesEntity.getThumbnail())
                .title(seriesEntity.getTitle())
                .introduceSentence(seriesEntity.getIntroduceSentence())
                .startDate(seriesEntity.getSeriesStartDate())
                .endDate(seriesEntity.getSeriesEndDate())
                .articleCount(seriesEntity.getArticleCount())
                .likes(seriesEntity.getLikes())
                .build(),
            SubscribeObject.builder()
                .startDate(seriesEntity.getSubscribeStartDate())
                .endDate(seriesEntity.getSubscribeEndDate())
                .status(String.valueOf(seriesEntity.getSubscribeStatus()))
                .build(),
            seriesEntity.getCategory(),
            WriterObject.builder()
                .id(writerResponse.writerId())
                .nickname(writerResponse.user().nickname())
                .build()
        );
    }

}
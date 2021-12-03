package com.prgrms.monthsub.converter;

import static com.prgrms.monthsub.common.utils.TimeUtil.convertUploadDateListToUploadDateString;

import com.prgrms.monthsub.domain.Series;
import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.domain.enumType.Category;
import com.prgrms.monthsub.domain.enumType.SeriesStatus;
import com.prgrms.monthsub.dto.request.SeriesSubscribePostRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.stereotype.Component;

@Component
public class SeriesConverter {

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
            .likes(0)
            .uploadDate(convertUploadDateListToUploadDateString(req.uploadDate()))
            .category(Category.of(req.category()))
            .uploadTime(LocalTime.parse(req.uploadTime()))
            .writer(writer)
            .build();
    }

}

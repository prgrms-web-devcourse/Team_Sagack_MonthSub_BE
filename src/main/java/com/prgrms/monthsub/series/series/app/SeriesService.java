package com.prgrms.monthsub.series.series.app;

import com.prgrms.monthsub.common.config.AWS;
import com.prgrms.monthsub.common.config.S3.Bucket;
import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.part.user.app.inferface.WriterProvider;
import com.prgrms.monthsub.part.writer.app.WriterService;
import com.prgrms.monthsub.part.writer.domain.Writer;
import com.prgrms.monthsub.series.article.app.ArticleService;
import com.prgrms.monthsub.series.article.domain.Article;
import com.prgrms.monthsub.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.series.series.domain.Series;
import com.prgrms.monthsub.series.series.domain.exception.SeriesException.SeriesNotFound;
import com.prgrms.monthsub.series.series.domain.type.SortType;
import com.prgrms.monthsub.series.series.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.series.series.dto.SeriesSubscribeList;
import com.prgrms.monthsub.series.series.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.series.series.dto.SeriesSubscribePost;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SeriesService {

    private final SeriesRepository seriesRepository;

    private final ArticleService articleService;

    private final WriterProvider writerProvider;

    private final S3Uploader s3Uploader;

    private final SeriesConverter seriesConverter;

    public SeriesService(SeriesRepository seriesRepository, ArticleService articleService,
        WriterService writerProvider,
        SeriesConverter seriesConverter,
        S3Uploader s3Uploader, AWS aws) {
        this.seriesRepository = seriesRepository;
        this.articleService = articleService;
        this.writerProvider = writerProvider;
        this.seriesConverter = seriesConverter;
        this.s3Uploader = s3Uploader;
    }

    public Series getSeriesById(Long id) {
        return seriesRepository
            .findById(id)
            .orElseThrow(() -> new SeriesNotFound("id=" + id));
    }

    @Transactional
    public SeriesSubscribePost.Response createSeries(Long userId, MultipartFile thumbnail,
        SeriesSubscribePost.Request request) throws IOException {
        String imageUrl = this.uploadThumbnailImage(thumbnail, userId);
        Writer writer = writerProvider.findByUserId(userId);
        Series entity = seriesConverter.SeriesSubscribePostResponseToEntity(
            writer, imageUrl, request);
        return new SeriesSubscribePost.Response(seriesRepository.save(entity).getId());
    }

    public SeriesSubscribeOne.Response getSeriesBySeriesId(Long seriesId) {
        List<Article> articleList = articleService.getArticleListBySeriesId(seriesId);
        Series series = getSeriesById(seriesId);
        return seriesConverter.seriesToSeriesOneResponse(series, articleList);
    }

    public List<SeriesSubscribeList.Response> getSeriesList() {
        List<Series> seriesList = seriesRepository.findSeriesList();
        return seriesList.stream().map(seriesConverter::seriesListToResponse)
            .collect(Collectors.toList());
    }

    public List<SeriesSubscribeList.Response> getSeriesListOrderBySort(SortType sort) {
        List<Series> seriesList;

        seriesList = switch (sort) {
            case RECENT -> seriesRepository.findSeriesListOrderByCreatedAt();
            case POPULAR -> seriesRepository.findSeriesListOrderByLike();
        };

        return seriesList.stream().map(seriesConverter::seriesListToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public SeriesSubscribeEdit.Response editSeries(Long seriesId, Long userId,
        MultipartFile thumbnail,
        SeriesSubscribeEdit.Request request) throws IOException {
        String imageUrl = this.uploadThumbnailImage(thumbnail, userId);

        Series series = this.getSeriesById(seriesId);

        series.editSeries(imageUrl, request);

        return new SeriesSubscribeEdit.Response(seriesRepository.save(series).getId());
    }

    public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
        Series series = this.getSeriesById(seriesId);

        return this.seriesConverter.seriesToResponseUsageEdit(series);
    }

    public String uploadThumbnailImage(MultipartFile image, Long id) throws IOException {
        String key = Series.class.getSimpleName().toLowerCase()
            + "/" + id.toString()
            + "/thumbnail/"
            + UUID.randomUUID() +
            s3Uploader.getExtension(image);

        return s3Uploader.upload(Bucket.IMAGE, image, key);
    }

    public String updateThumbnailImage(MultipartFile image, Long id) throws IOException {
        Series series = this.seriesRepository.getById(id);

        String thumbnailKey = this.uploadThumbnailImage(image, series.getId());
        // series changeThumbnailKey() 가 필요 합니다.
        return this.seriesRepository.save(series).getThumbnailKey();
    }

}

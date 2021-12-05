package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.exception.domain.series.SeriesException.SeriesNotFound;
import com.prgrms.monthsub.converter.SeriesConverter;
import com.prgrms.monthsub.domain.Article;
import com.prgrms.monthsub.domain.Series;
import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.dto.SeriesSubscribeEdit;
import com.prgrms.monthsub.dto.SeriesSubscribeList;
import com.prgrms.monthsub.dto.SeriesSubscribeOne;
import com.prgrms.monthsub.dto.SeriesSubscribePost;
import com.prgrms.monthsub.repository.SeriesRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class SeriesService {

    private static final String DIRECTORY = "seriesThumbnails";

    private final SeriesRepository seriesRepository;

    private final ArticleService articleService;

    private final WriterService writerService;

    private final S3UploaderService s3Uploader;

    private final SeriesConverter seriesConverter;

    public SeriesService(SeriesRepository seriesRepository, ArticleService articleService,
        WriterService writerService,
        SeriesConverter seriesConverter,
        S3UploaderService s3Uploader) {
        this.seriesRepository = seriesRepository;
        this.articleService = articleService;
        this.writerService = writerService;
        this.seriesConverter = seriesConverter;
        this.s3Uploader = s3Uploader;
    }

    @Transactional
    public SeriesSubscribePost.Response createSeries(Long userId, MultipartFile thumbnail,
        SeriesSubscribePost.Request request) throws IOException {
        String imageUrl = s3Uploader.upload(thumbnail, DIRECTORY);
        Writer writer = writerService.findByUserId(userId);
        Series entity = seriesConverter.SeriesSubscribePostResponseToEntity(
            writer, imageUrl, request);
        return new SeriesSubscribePost.Response(seriesRepository.save(entity).getId());
    }

    public SeriesSubscribeOne.Response getSeriesBySeriesId(Long seriesId) {
        List<Article> articleList = articleService.getArticleListBySeriesId(seriesId);
        return seriesRepository.findSeriesById(seriesId)
            .map(series -> seriesConverter.seriesToSeriesOneResponse(series, articleList))
            .orElseThrow(() -> new SeriesNotFound("seriesId=" + seriesId));
    }

    public List<SeriesSubscribeList.Response> getSeriesList() {
        List<Series> seriesList = seriesRepository.findSeriesList();
        return seriesList.stream().map(seriesConverter::seriesListToResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public SeriesSubscribeEdit.Response editSeries(Long seriesId, MultipartFile thumbnail,
        SeriesSubscribeEdit.Request request) throws IOException {
        String imageUrl = !thumbnail.isEmpty() ? s3Uploader.upload(thumbnail, DIRECTORY) : null;
        Series series = seriesRepository.findSeriesById(seriesId)
            .orElseThrow(() -> new SeriesNotFound("seriesId=" + seriesId));
        series.editSeries(imageUrl, request);
        return new SeriesSubscribeEdit.Response(seriesRepository.save(series).getId());
    }

    public SeriesSubscribeOne.ResponseUsageEdit getSeriesUsageEdit(Long seriesId) {
        return seriesRepository.findById(seriesId)
            .map(seriesConverter::seriesToResponseUsageEdit)
            .orElseThrow(EntityNotFoundException::new);
    }

}

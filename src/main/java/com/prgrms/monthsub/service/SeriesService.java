package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.exception.EntityNotFoundException;
import com.prgrms.monthsub.converter.SeriesConverter;
import com.prgrms.monthsub.domain.Article;
import com.prgrms.monthsub.domain.Series;
import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.dto.request.SeriesSubscribePostRequest;
import com.prgrms.monthsub.dto.response.SeriesOneResponse;
import com.prgrms.monthsub.dto.response.SeriesSubscribePostResponse;
import com.prgrms.monthsub.repository.ArticleRepository;
import com.prgrms.monthsub.repository.SeriesRepository;
import java.io.IOException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class SeriesService {

    private static final String DIRECTORY = "seriesThumbnails";

    private final SeriesRepository seriesRepository;

    private final ArticleRepository articleRepository;

    private final WriterService writerService;

    private final S3Uploader s3Uploader;

    private final SeriesConverter seriesConverter;

    public SeriesService(SeriesRepository seriesRepository,
        ArticleRepository articleRepository, WriterService writerService,
        SeriesConverter seriesConverter,
        S3Uploader s3Uploader) {
        this.seriesRepository = seriesRepository;
        this.articleRepository = articleRepository;
        this.writerService = writerService;
        this.seriesConverter = seriesConverter;
        this.s3Uploader = s3Uploader;
    }

    @Transactional
    public SeriesSubscribePostResponse createSeries(Long userId, MultipartFile thumbnail,
        SeriesSubscribePostRequest request) throws IOException {
        String imageUrl = s3Uploader.upload(thumbnail, DIRECTORY);
        Writer writer = writerService.findByUserId(userId);
        Series entity = seriesConverter.SeriesSubscribePostResponseToEntity(writer, imageUrl, request);
        return new SeriesSubscribePostResponse(seriesRepository.save(entity).getId());
    }

    public SeriesOneResponse getSeriesBySeriesId(Long seriesId) {
        List<Article> articleList = articleRepository.findAllArticleBySeriesId(seriesId);
        return seriesRepository.findSeriesById(seriesId)
            .map(series -> seriesConverter.seriesToSeriesOneResponse(series, articleList))
            .orElseThrow(EntityNotFoundException::new);
    }

}

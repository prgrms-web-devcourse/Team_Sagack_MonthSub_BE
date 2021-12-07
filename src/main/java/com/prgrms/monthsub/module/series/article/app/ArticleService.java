package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.config.S3.Bucket;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.article.dto.ArticlePost;
import com.prgrms.monthsub.module.series.series.app.SeriesRepository;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class ArticleService {

    private final ArticleRepository articleRepository;

    //SeriesService, ArticleService 순환참조 문제로 레포 바로 접근.
    private final SeriesRepository seriesRepository;

    private final ArticleConverter articleConverter;

    private final S3Uploader s3Uploader;

    public ArticleService(ArticleRepository articleRepository,
        SeriesRepository seriesRepository,
        ArticleConverter articleConverter,
        S3Uploader s3Uploader) {
        this.articleRepository = articleRepository;
        this.seriesRepository = seriesRepository;
        this.articleConverter = articleConverter;
        this.s3Uploader = s3Uploader;
    }

    public List<Article> getArticleListBySeriesId(Long seriesId) {
        return articleRepository.findAllArticleBySeriesId(seriesId);
    }

    @Transactional
    public ArticlePost.Response createArticle(Long seriesId, MultipartFile thumbnail,
        ArticlePost.Request request) throws IOException {
        Series series = seriesRepository.findById(seriesId).orElseThrow(() ->
            new SeriesNotFound("seriesId=" + seriesId));

        Long articleCount = articleRepository.countBySeriesId(seriesId);
        Article entity = articleConverter.ArticlePostToEntity(series, request, articleCount.intValue() + 1);
        Long id = articleRepository.save(entity).getId();
        entity.changeThumbnailKey(this.uploadThumbnailImage(thumbnail, seriesId, id));

        return new ArticlePost.Response(id);
    }

    private String uploadThumbnailImage(MultipartFile image, Long seriesId, Long id) throws IOException {
        String key = Series.class.getSimpleName().toLowerCase()
            + "/" + seriesId.toString()
            + "/" + Article.class.getSimpleName().toLowerCase()
            + "/" + id.toString()
            + "/thumbnail/"
            + UUID.randomUUID() +
            s3Uploader.getExtension(image);

        return s3Uploader.upload(Bucket.IMAGE, image, key);
    }

}

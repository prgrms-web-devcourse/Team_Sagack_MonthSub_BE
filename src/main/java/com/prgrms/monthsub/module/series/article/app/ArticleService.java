package com.prgrms.monthsub.module.series.article.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.config.S3.Bucket;
import com.prgrms.monthsub.module.series.article.converter.ArticleConverter;
import com.prgrms.monthsub.module.series.article.domain.Article;
import com.prgrms.monthsub.module.series.series.domain.Series;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final S3Uploader s3Uploader;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleConverter articleConverter,
            S3Uploader s3Uploader
    ) {
        this.articleRepository = articleRepository;
        this.s3Uploader = s3Uploader;
    }

    public Long save(Article article) {
        return this.articleRepository.save(article).getId();
    }

    public List<Article> getArticleListBySeriesId(Long seriesId) {
        return this.articleRepository.findAllArticleBySeriesId(seriesId);
    }

    public Long countBySeriesId(Long seriesId) {
        return this.articleRepository.countBySeriesId(seriesId);
    }

    protected String uploadThumbnailImage(
            MultipartFile image,
            Long seriesId,
            Long id
    ) throws IOException {
        String key = Series.class.getSimpleName()
                .toLowerCase()
                + "/" + seriesId.toString()
                + "/" + Article.class.getSimpleName()
                .toLowerCase()
                + "/" + id.toString()
                + "/thumbnail/"
                + UUID.randomUUID() +
                this.s3Uploader.getExtension(image);

        return this.s3Uploader.upload(Bucket.IMAGE, image, key, S3Uploader.imageExtensions);
    }

}

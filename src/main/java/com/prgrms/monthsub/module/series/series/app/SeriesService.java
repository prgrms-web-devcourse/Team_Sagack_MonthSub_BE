package com.prgrms.monthsub.module.series.series.app;

import static com.prgrms.monthsub.module.series.series.domain.Series.Category.getCategories;

import com.prgrms.monthsub.module.series.series.app.Provider.SeriesProvider;
import com.prgrms.monthsub.module.series.series.converter.SeriesConverter;
import com.prgrms.monthsub.module.series.series.domain.ArticleUploadDate;
import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import com.prgrms.monthsub.module.series.series.domain.Series.SeriesStatus;
import com.prgrms.monthsub.module.series.series.domain.exception.SeriesException.SeriesNotFound;
import com.prgrms.monthsub.module.series.series.dto.SeriesSubscribeList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SeriesService implements SeriesProvider {

  private final SeriesRepositoryCustom seriesRepository;
  private final SeriesConverter seriesConverter;
  private final ArticleUploadDateRepository articleUploadDateRepository;
  private List<Category> categoryList;

  public SeriesService(
    SeriesRepositoryCustom seriesRepository,
    SeriesConverter seriesConverter,
    ArticleUploadDateRepository articleUploadDateRepository
  ) {
    this.seriesRepository = seriesRepository;
    this.seriesConverter = seriesConverter;
    this.articleUploadDateRepository = articleUploadDateRepository;
  }

  @Override
  public Series getById(Long id) {
    return this.seriesRepository
      .findById(id)
      .orElseThrow(() -> new SeriesNotFound("id=" + id));
  }

  @Transactional
  public Long save(Series series) {
    return this.seriesRepository.save(series).getId();
  }

  @Transactional
  public void articleUploadDateSave(ArticleUploadDate articleUploadDate) {
    this.articleUploadDateRepository.save(articleUploadDate);
  }

  @Override
  public List<ArticleUploadDate> getArticleUploadDate(Long seriesId) {
    return this.articleUploadDateRepository.findAllBySeriesId(seriesId);
  }

  public boolean checkSeriesStatusByWriterId(
    Long writerId,
    SeriesStatus status
  ) {
    return this.seriesRepository.existsAllByWriterIdAndSubscribeStatus(writerId, status);
  }

  public List<Series> findAllByWriterId(
    Long writerId
  ) {
    return this.seriesRepository.findAllByWriterId(writerId);
  }

  public List<Series> getSeriesSearchTitle(String title) {
    return this.seriesRepository.findByTitleContainingIgnoreCase(title);
  }

  public List<Series> findAll(Sort sort) {
    return this.seriesRepository.findAll(sort);
  }

  public List<Series> findAll(Pageable pageable) {
    return this.seriesRepository.findAll(pageable).getContent();
  }

  public List<Series> findBySubscribeStatus(
    SeriesStatus status,
    Pageable pageable
  ) {
    return this.seriesRepository.findBySubscribeStatus(status, pageable);
  }

  public List<Series> getSeriesByCategoriesLessThanId(
    Long id,
    List<Category> categories,
    Pageable pageable
  ) {
    return this.seriesRepository.findAllByIdLessThanAndCategoryIn(id, categories, pageable);
  }

  public List<Series> getSeries(
    Long id,
    Pageable pageable
  ) {
    return this.seriesRepository.findByIdLessThan(id, pageable);
  }

  public SeriesSubscribeList.Response getSeriesList(
    Optional<Long> lastSeriesId,
    Integer size,
    List<Category> categories
  ) {
    categoryList = (categories.contains(Category.ALL)) ? getCategories() : categories;
    return new SeriesSubscribeList.Response(
      lastSeriesId.map(id -> {
          LocalDateTime createdAt = this.seriesRepository.getById(id).getCreatedAt();
          return this.seriesRepository.findAllByCategory(
            id,
            size,
            categoryList,
            createdAt
          );
        }).orElse(
          this.seriesRepository.findAllByCategoryIn(
            categoryList, PageRequest.of(
              0,
              size,
              Sort.by(Direction.DESC, "createdAt", "id"))
          ))
        .stream()
        .map(this.seriesConverter::toResponse)
        .collect(Collectors.toList()));
  }

}

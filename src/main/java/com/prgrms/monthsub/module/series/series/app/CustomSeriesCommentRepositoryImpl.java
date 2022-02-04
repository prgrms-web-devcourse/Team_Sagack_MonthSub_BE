package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class CustomSeriesCommentRepositoryImpl implements DynamicSeriesCommentRepository {

  @PersistenceContext
  private final EntityManager em;

  public CustomSeriesCommentRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  public List<SeriesComment> findAll(
    Long seriesId,
    Long lastId,
    int size,
    LocalDateTime createdAt
  ) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<SeriesComment> criteria = builder.createQuery(SeriesComment.class);
    Root<SeriesComment> seriesComment = criteria.from(SeriesComment.class);

    Predicate equalSeriesId = builder.equal(seriesComment.get("seriesId"), seriesId);
    Predicate nullParentId = builder.isNull(seriesComment.get("parentId"));
    Predicate equalCreatedAt = builder.equal(seriesComment.get("createdAt"), createdAt);
    Predicate lessThanId = builder.lessThan(seriesComment.get("id"), lastId);
    Predicate lessThanCreatedAt = builder.lessThan(seriesComment.get("createdAt"), createdAt);

    Predicate where = builder.and(
      equalSeriesId, builder.and(
        nullParentId,
        builder.or(
          lessThanCreatedAt,
          builder.and(equalCreatedAt, lessThanId)
        )
      )
    );

    Order[] orders = {
      builder.desc(seriesComment.get("createdAt")),
      builder.desc(seriesComment.get("id"))
    };

    CriteriaQuery<SeriesComment> query = criteria.select(seriesComment)
      .where(where)
      .orderBy(orders);

    return em.createQuery(query)
      .setMaxResults(size)
      .getResultList();
  }

}

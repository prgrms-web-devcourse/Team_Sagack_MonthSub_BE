package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.Series;
import com.prgrms.monthsub.module.series.series.domain.Series.Category;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultSeriesRepository implements CustomServiceRepository {

  //@PersistenceUnit

  private final EntityManagerFactory emf;
  private final EntityManager em;

  protected DefaultSeriesRepository(
    EntityManagerFactory emf,
    EntityManager em
  ) {
    this.emf = emf;
    this.em = em;
  }

  @Override
  public List<Series> findAllByCategoryIn(
    Long lastSeriesId,
    int size,
    List<Category> categories,
    LocalDateTime createdAt
  ) {
    EntityManager em = emf.createEntityManager();
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<Series> criteria = builder.createQuery(Series.class);
    Root<Series> series = criteria.from(Series.class);

    Predicate inCategory = series.get("category").in(categories);
    Predicate equalCreatedAt = builder.equal(series.get("createdAt"), createdAt);
    Predicate lessThanId = builder.lessThan(series.get("id"), lastSeriesId);
    Predicate lessThanCreatedAt = builder.lessThan(series.get("createdAt"), createdAt);

    Predicate where = builder.and(
      inCategory,
      builder.or(
        lessThanCreatedAt,
        builder.and(equalCreatedAt, lessThanId)
      )
    );

    Order[] orders = {
      builder.desc(series.get("createdAt")),
      builder.desc(series.get("id"))
    };

    CriteriaQuery<Series> query = criteria.select(series).where(where).orderBy(orders);
    return em.createQuery(query).setMaxResults(size).getResultList();
  }

}

package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
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
public class CustomWriterLikesRepositoryImpl implements DynamicWriterLikesRepository {

  @PersistenceContext
  private final EntityManager em;

  public CustomWriterLikesRepositoryImpl(EntityManager em) {
    this.em = em;
  }

  @Override
  public List<WriterLikes> findAll(
    Long userId,
    Long lastId,
    int size,
    LocalDateTime createdAt
  ) {
    CriteriaBuilder builder = em.getCriteriaBuilder();
    CriteriaQuery<WriterLikes> criteria = builder.createQuery(WriterLikes.class);
    Root<WriterLikes> writerLikes = criteria.from(WriterLikes.class);

    Predicate inLikesStatus = writerLikes.get("likesStatus").in(LikesStatus.Like);
    Predicate equalUserId = builder.equal(writerLikes.get("userId"), userId);
    Predicate equalCreatedAt = builder.equal(writerLikes.get("createdAt"), createdAt);
    Predicate lessThanId = builder.lessThan(writerLikes.get("id"), lastId);
    Predicate lessThanCreatedAt = builder.lessThan(writerLikes.get("createdAt"), createdAt);

    Predicate where = builder.and(
      inLikesStatus, builder.and(
        equalUserId, builder.or(
          lessThanCreatedAt,
          builder.and(equalCreatedAt, lessThanId)
        )
      )
    );

    Order[] orders = {
      builder.desc(writerLikes.get("createdAt")),
      builder.desc(writerLikes.get("id"))
    };

    CriteriaQuery<WriterLikes> query = criteria.select(writerLikes).where(where).orderBy(orders);

    return em.createQuery(query)
      .setMaxResults(size)
      .getResultList();
  }

}

package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterLikesRepository extends JpaRepository<WriterLikes, Long> {

  Optional<WriterLikes> findByUserIdAndWriterId(
    Long userId,
    Long WriterId
  );

  List<WriterLikes> findAllByUserIdAndLikesStatus(
    Long userId,
    LikesStatus likesStatus,
    Pageable pageable
  );

  List<WriterLikes> findAllByUserIdAndLikesStatus(
    Long userId,
    LikesStatus likesStatus
  );

  List<WriterLikes> findByIdLessThanAndUserIdAndLikesStatus(
    Long id,
    Long userId,
    LikesStatus likesStatus,
    Pageable pageable
  );

}

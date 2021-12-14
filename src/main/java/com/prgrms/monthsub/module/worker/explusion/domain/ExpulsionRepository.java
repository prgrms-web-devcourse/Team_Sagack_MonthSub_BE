package com.prgrms.monthsub.module.worker.explusion.domain;

import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.Status;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpulsionRepository extends JpaRepository<Expulsion, Long> {

  List<Expulsion> findAllByStatus(
    Status status,
    Pageable pageable
  );

  long countByStatus(Status status);

}

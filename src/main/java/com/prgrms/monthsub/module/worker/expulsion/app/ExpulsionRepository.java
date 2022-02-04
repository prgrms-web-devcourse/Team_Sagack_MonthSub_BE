package com.prgrms.monthsub.module.worker.expulsion.app;

import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.Status;
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

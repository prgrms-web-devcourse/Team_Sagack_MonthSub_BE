package com.prgrms.monthsub.repository;

import com.prgrms.monthsub.domain.Writer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepository extends JpaRepository<Writer, Long> {

    Optional<Writer> findByUserId(Long userId);

}
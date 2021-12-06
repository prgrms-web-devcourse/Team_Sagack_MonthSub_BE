package com.prgrms.monthsub.part.writer.app;

import com.prgrms.monthsub.part.writer.domain.Writer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WriterRepository extends JpaRepository<Writer, Long> {

    Optional<Writer> findByUserId(Long userId);

}

package com.prgrms.monthsub.part.user.app;

import com.prgrms.monthsub.part.user.domain.Part;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByName(String name);

}

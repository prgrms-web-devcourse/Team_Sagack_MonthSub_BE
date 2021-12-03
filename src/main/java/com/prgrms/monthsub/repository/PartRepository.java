package com.prgrms.monthsub.repository;

import com.prgrms.monthsub.domain.Part;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {

    Optional<Part> findByName(String name);

}

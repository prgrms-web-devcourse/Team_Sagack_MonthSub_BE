package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesUserRepository extends JpaRepository<SeriesUser, Long> {

  List<SeriesUser> findAllByUserId(Long userId);

}

package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesCommentRepository extends JpaRepository<SeriesComment, Long> {
}

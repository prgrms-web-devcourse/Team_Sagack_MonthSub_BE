package com.prgrms.monthsub.module.series.series.app;

import com.prgrms.monthsub.module.series.series.domain.SeriesUser;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesUserRepository extends JpaRepository<SeriesUser, Long> {

    @Query("select su from SeriesUser as su join fetch su.series sus where su.userId = :userId")
    List<SeriesUser> findAllMySubscribeByUserId(@Param("userId") Long userId);

}

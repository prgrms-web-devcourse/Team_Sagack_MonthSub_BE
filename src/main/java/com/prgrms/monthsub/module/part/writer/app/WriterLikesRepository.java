package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WriterLikesRepository extends JpaRepository<WriterLikes, Long> {

    @Query("select wl from WriterLikes as wl join fetch wl.writer as wlw join fetch wlw.user where wl.userId = :userId and wl.likesStatus = :likesStatus")
    List<WriterLikes> findAllByUserId(@Param("userId") Long userId,
        @Param("likesStatus") LikesStatus likesStatus);

}

package com.prgrms.monthsub.module.part.writer.app.provider;

import com.prgrms.monthsub.module.part.writer.domain.WriterLikes;
import com.prgrms.monthsub.module.part.writer.domain.WriterLikes.LikesStatus;
import java.util.List;

public interface WriterLikesProvider {

  List<WriterLikes> getFollowWriterList(
    Long userId,
    LikesStatus likesStatus
  );

}

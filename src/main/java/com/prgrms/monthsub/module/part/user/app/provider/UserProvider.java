package com.prgrms.monthsub.module.part.user.app.provider;

import com.prgrms.monthsub.module.part.user.domain.User;
import java.util.List;
import java.util.Optional;

public interface UserProvider {

  Optional<User> findByNickname(String nickName);

  User findById(Long id);

  List<User> findByIdIn(List<Long> ids);

}

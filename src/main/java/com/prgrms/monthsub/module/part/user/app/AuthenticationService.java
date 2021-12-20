package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.common.exception.global.AuthenticationException.UserNotExist;
import com.prgrms.monthsub.module.part.user.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

  private final UserRepository userRepository;

  public AuthenticationService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User findByEmail(String email) {
    return this.userRepository
      .findByEmail(email)
      .orElseThrow(() -> new UserNotExist("email=" + email));
  }

}

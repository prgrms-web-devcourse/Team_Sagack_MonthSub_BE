package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.exception.global.AuthenticationException.UserNotExist;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticationService {

    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserName(String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(() -> new UserNotExist("username=" + username));
    }

}

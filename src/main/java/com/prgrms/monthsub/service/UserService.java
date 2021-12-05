package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.EntityNotFoundException;
import com.prgrms.monthsub.common.error.exception.user.UserException.UserNotFound;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User login(String email, String credentials) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFound("email=" + email));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCodes.ENTITY_NOT_FOUND("id=" + userId)));
    }

}

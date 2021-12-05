package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.exception.domain.user.UserException.UserNotExist;
import com.prgrms.monthsub.common.error.exception.domain.user.UserException.UserNotFound;
import com.prgrms.monthsub.converter.UserConverter;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.UserSignUp;
import com.prgrms.monthsub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final UserConverter userConverter;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository, UserConverter userConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userConverter = userConverter;
    }

    public User login(String email, String credentials) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotExist("email=" + email));
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFound("id=" + userId));
    }

    @Transactional
    public UserSignUp.Response signUp(UserSignUp.Request request) {
        User entity = userRepository.save(userConverter.UserSignUpRequestToEntity(request));
        return new UserSignUp.Response(userRepository.save(entity).getId());
    }

}

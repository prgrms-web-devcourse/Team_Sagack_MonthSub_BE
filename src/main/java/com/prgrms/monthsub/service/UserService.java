package com.prgrms.monthsub.service;


import com.prgrms.monthsub.common.error.exception.UserNotFoundException;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public User login(String email, String credentials) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(UserNotFoundException::new);
        user.checkPassword(passwordEncoder, credentials);
        return user;
    }

}

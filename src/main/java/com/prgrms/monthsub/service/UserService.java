package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.exception.domain.user.UserException.EmailDuplicated;
import com.prgrms.monthsub.common.error.exception.domain.user.UserException.NickNameDuplicated;
import com.prgrms.monthsub.common.error.exception.domain.user.UserException.UserNotExist;
import com.prgrms.monthsub.common.error.exception.domain.user.UserException.UserNotFound;
import com.prgrms.monthsub.converter.UserConverter;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.dto.UserSignUp;
import com.prgrms.monthsub.repository.UserRepository;
import java.util.Optional;
import java.io.IOException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final S3UploaderService s3UploaderService;

    private final UserConverter userConverter;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository, S3UploaderService s3UploaderService,
        UserConverter userConverter) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.s3UploaderService = s3UploaderService;
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
        checkEmail(request.email());
        checkNicName(request.nickName());
        User entity = userRepository.save(userConverter.UserSignUpRequestToEntity(request));
        return new UserSignUp.Response(userRepository.save(entity).getId());
    }

    public String uploadImage(MultipartFile image, Long userId, String imagePurpose) throws IOException {
        String imageUrl = s3UploaderService.upload(image, User.class.getSimpleName(), userId, imagePurpose);
        if (imageUrl == null) {
            //Todo profile field null로 갱신
        }

        return imageUrl;
    }


    private void checkEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new EmailDuplicated("email = " + email);
        }
    }

    private void checkNicName(String nickName) {
        Optional<User> user = userRepository.findByNickname(nickName);
        if (user.isPresent()) {
            throw new NickNameDuplicated("nickName = " + nickName);
        }

    }

}

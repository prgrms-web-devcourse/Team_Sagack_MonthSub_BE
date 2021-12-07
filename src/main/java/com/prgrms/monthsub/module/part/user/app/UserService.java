package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.common.utils.S3Uploader;
import com.prgrms.monthsub.config.AWS;
import com.prgrms.monthsub.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.EmailDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.NickNameDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.UserNotExist;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.UserNotFound;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class UserService {


    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final S3Uploader s3Uploader;

    private final UserConverter userConverter;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository, S3Uploader s3Uploader,
        UserConverter userConverter, AWS aws) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.s3Uploader = s3Uploader;
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

    public String uploadProfileImage(MultipartFile image, Long userId)
        throws IOException {

        String key = User.class.getSimpleName().toLowerCase()
            + "/" + userId.toString()
            + "/profile/"
            + UUID.randomUUID()
            + s3Uploader.getExtension(image);

        String imageUrl = s3Uploader.upload(Bucket.IMAGE, image, key);

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

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
import com.prgrms.monthsub.module.part.user.dto.UserEdit;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageName;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageStatus;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.io.IOException;
import java.time.LocalDateTime;
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

    private final ExpulsionService expulsionService;

    private final S3Uploader s3Uploader;

    private final UserConverter userConverter;

    public UserService(PasswordEncoder passwordEncoder,
        UserRepository userRepository, S3Uploader s3Uploader,
        UserConverter userConverter, AWS aws,
        ExpulsionService expulsionService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.s3Uploader = s3Uploader;
        this.userConverter = userConverter;
        this.expulsionService = expulsionService;
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
        return new UserSignUp.Response(entity.getId());
    }

    @Transactional
    public UserEdit.Response edit(Long userId, UserEdit.Request request) {
        checkNicName(request.nickName());
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotExist("userId=" + userId));
        user.editUser(request.nickName(), request.profileIntroduce());
        return new UserEdit.Response(userRepository.save(user).getId());
    }


    @Transactional
    public String uploadProfileImage(MultipartFile image, Long userId)
        throws IOException {

        String key = User.class.getSimpleName().toLowerCase()
            + "s"
            + "/" + userId.toString()
            + "/profile/"
            + UUID.randomUUID()
            + s3Uploader.getExtension(image);

        String imageUrl = this.s3Uploader.upload(Bucket.IMAGE, image, key);

        User user = this.userRepository.findById(userId)
            .orElseThrow(() -> new UserNotExist("userId=" + userId));

        String originalProfile = user.getProfileKey();
        user.changeProfileKey(key);

        if (originalProfile != null) {
            Expulsion expulsion = Expulsion.builder()
                .userId(user.getId())
                .imageKey(key)
                .expulsionImageStatus(ExpulsionImageStatus.CREATED)
                .expulsionImageName(ExpulsionImageName.USER_PROFILE)
                .hardDeleteDate(LocalDateTime.now())
                .build();
            expulsionService.save(expulsion);
        }

        return this.userConverter.UserProfile(imageUrl);
    }


    private void checkEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new EmailDuplicated("email = " + email);
        }
    }

    private void checkNicName(String nickName) {
        Optional<User> user = this.userRepository.findByNickname(nickName);
        if (user.isPresent()) {
            throw new NickNameDuplicated("nickName = " + nickName);
        }

    }

}

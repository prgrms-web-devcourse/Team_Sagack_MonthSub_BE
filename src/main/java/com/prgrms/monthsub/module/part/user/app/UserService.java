package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.EmailDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.NickNameDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.UserNotFound;
import com.prgrms.monthsub.module.part.user.dto.UserEdit;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.Status;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class UserService implements UserProvider {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final ExpulsionService expulsionService;
  private final S3Client s3Client;
  private final UserConverter userConverter;

  public UserService(
    PasswordEncoder passwordEncoder,
    UserRepository userRepository,
    S3Client s3Client,
    UserConverter userConverter,
    ExpulsionService expulsionService
  ) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.s3Client = s3Client;
    this.userConverter = userConverter;
    this.expulsionService = expulsionService;
  }

  @Override
  public Optional<User> findByNickname(String nickname) {
    return this.userRepository
      .findByNickname(nickname);
  }

  @Override
  public User findById(Long userId) {
    return this.userRepository
      .findById(userId)
      .orElseThrow(() -> new UserNotFound("id=" + userId));
  }

  public User findByEmail(String email) {
    return this.userRepository
      .findByEmail(email)
      .orElseThrow(() -> new UserNotFound("email=" + email));
  }

  public User login(
    String email,
    String credentials
  ) {
    User user = this.findByEmail(email);
    user.checkPassword(this.passwordEncoder, credentials);

    return user;
  }

  @Transactional
  public UserSignUp.Response signUp(UserSignUp.Request request) {
    checkEmail(request.email());
    checkNicName(request.nickName());
    User entity = this.userRepository.save(this.userConverter.UserSignUpRequestToEntity(request));
    return new UserSignUp.Response(entity.getId());
  }

  @Transactional
  public UserEdit.Response edit(
    Long id,
    UserEdit.Request request
  ) {
    checkNicName(request.nickName());
    User user = this.findById(id);
    user.editUser(request.nickName(), request.profileIntroduce());

    return new UserEdit.Response(this.userRepository.save(user)
      .getId());
  }

  @Transactional
  public String uploadProfileImage(
    Optional<MultipartFile> image,
    Long userId
  ) {
    User user = this.findById(userId);

    String profileKey = image.map(imageFile -> {
          if (imageFile.isEmpty()) {
            return null;
          }

          String key = User.class.getSimpleName()
            .toLowerCase()
            + "s"
            + "/" + userId.toString()
            + "/profile/"
            + UUID.randomUUID()
            + this.s3Client.getExtension(imageFile);

          return this.s3Client.upload(
            Bucket.IMAGE,
            imageFile,
            key,
            S3Client.imageExtensions
          );
        }
      )
      .orElse(null);

    String originalProfileKey = user.getProfileKey();

    if (originalProfileKey != null) {
      expulsionService.save(
        user.getId(), originalProfileKey, Status.CREATED,
        FileCategory.USER_PROFILE
      );
    }

    user.changeProfileKey(profileKey);

    return this.userConverter.UserProfile(
      Optional.ofNullable(user.getProfileKey())
    );
  }

  private void checkEmail(String email) {
    this.userRepository
      .findByEmail(email)
      .map(user -> {
        throw new EmailDuplicated("email = " + email);
      });
  }

  private void checkNicName(String nickName) {
    this.userRepository
      .findByNickname(nickName)
      .map(user -> {
        throw new NickNameDuplicated("nickName = " + nickName);
      });
  }

}

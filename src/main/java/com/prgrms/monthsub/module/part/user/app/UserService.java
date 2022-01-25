package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.module.part.user.app.provider.UserProvider;
import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.EmailDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.NickNameDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.UserNotFound;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService implements UserProvider {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final UserConverter userConverter;

  public UserService(
    PasswordEncoder passwordEncoder,
    UserRepository userRepository,
    UserConverter userConverter
  ) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
    this.userConverter = userConverter;
  }

  @Override
  public Optional<User> findByNickname(String nickname) {
    return this.userRepository.findByNickname(nickname);
  }

  @Override
  public User findById(Long userId) {
    return this.userRepository
      .findById(userId)
      .orElseThrow(() -> new UserNotFound("id=" + userId));
  }

  @Override
  public List<User> findByIdIn(List<Long> ids){
    return this.userRepository.findByIdIn(ids);
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
    checkNickName(request.nickName());
    User user = this.userRepository.save(this.userConverter.toEntity(request));

    return new UserSignUp.Response(user.getId());
  }

  protected void checkEmail(String email) {
    this.userRepository
      .findByEmail(email)
      .map(user -> {
        throw new EmailDuplicated("email = " + email);
      });
  }

  protected void checkNickName(String nickName) {
    this.userRepository
      .findByNickname(nickName)
      .map(user -> {
        throw new NickNameDuplicated("nickName = " + nickName);
      });
  }

  protected void checkNickName(
    String nickName,
    Long id
  ) {
    this.userRepository.findByNickname(nickName)
      .map(user -> {
        if (!user.getId().equals(id)) {
          throw new NickNameDuplicated("nickName = " + nickName);
        }
        return null;
      });
  }

}

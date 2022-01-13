package com.prgrms.monthsub.module.part.user.app;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.module.part.user.converter.UserConverter;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.EmailDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.NickNameDuplicated;
import com.prgrms.monthsub.module.part.user.domain.exception.UserException.UserNotFound;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp;
import com.prgrms.monthsub.module.part.user.dto.UserSignUp.Request;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserConverter userConverter;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserRepository userRepository;

  public static User getUser() {
    User user = Mockito.mock(User.class);
    given(user.getId()).willReturn(1L);
    given(user.getNickname()).willReturn("email");
    given(user.getUsername()).willReturn("userName");
    given(user.getNickname()).willReturn("nickName");
    return user;
  }

  @Test
  @DisplayName("닉네임으로 유저를 조회할 수 있다.")
  public void getUserNickNameTest() {
    //given
    User user = getUser();
    given(this.userRepository.findByNickname(anyString())).willReturn(Optional.of(user));

    //when
    Optional<User> userResponse = this.userService.findByNickname("nickName");

    //then
    assertThat(userResponse.get().getNickname(), is(user.getNickname()));
  }

  @Test
  @DisplayName("아이디로 유저를 조회할 수 있다.")
  public void getUserIdTest() {
    //given
    User user = getUser();
    given(this.userRepository.findById(anyLong())).willReturn(Optional.of(user));

    //when
    User userResponse = this.userService.findById(1L);

    //then
    assertThat(userResponse.getId(), is(user.getId()));
  }

  @Test
  @DisplayName("아이디로 유저를 조회할 수 없을 시 예외가 발생한다.")
  public void userNotFoundIdTest() {
    //given
    given(this.userRepository.findById(anyLong())).willReturn(Optional.empty());

    //when, then
    Assertions.assertThrows(
      UserNotFound.class, () -> this.userService.findById(1L));
  }

  @Test
  @DisplayName("이메일로 유저를 조회할 수 있다.")
  public void getUserEmailTest() {
    //given
    User user = getUser();
    given(this.userRepository.findByEmail(anyString())).willReturn(Optional.of(user));

    //when
    User userResponse = this.userService.findByEmail("email");

    //then
    assertThat(userResponse.getEmail(), is(user.getEmail()));
  }

  @Test
  @DisplayName("이메일로 유저를 조회할 수 없을 시 예외가 발생한다.")
  public void userNotFoundEmailTest() {
    //given
    given(this.userRepository.findByEmail(anyString())).willReturn(Optional.empty());

    //when, then
    Assertions.assertThrows(
      UserNotFound.class, () -> this.userService.findByEmail("email"));
  }

  @Test
  @DisplayName("로그인을 할 수 있다.")
  public void LoginTest() {
    //given
    User user = getUser();
    given(this.userRepository.findByEmail(anyString())).willReturn(Optional.of(user));
    given(!passwordEncoder.matches(anyString(), anyString())).willReturn(false);

    //when
    User userResponse = this.userService.login("email", "user123");

    //then
    assertThat(userResponse.getId(), is(1L));
  }

  @Test
  @DisplayName("회원가입을 할 수 있다.")
  public void signUpTest() {
    //given
    User user = getUser();
    UserSignUp.Request request = Request.builder()
      .email(user.getEmail())
      .userName(user.getUsername())
      .password(user.getPassword())
      .nickName(user.getNickname())
      .build();

    when(this.userRepository.save(any())).thenReturn(user);

    //when
    UserSignUp.Response response = this.userService.signUp(request);

    //then
    assertThat(response.userId(), is(user.getId()));
  }

  @Test
  @DisplayName("회원가입 시 이메일 중복은 예외가 발생한다.")
  public void duplicatedEmailTest() {
    //given
    User user = getUser();
    UserSignUp.Request request = Request.builder()
      .email(user.getEmail())
      .build();

    doReturn(Optional.of(user)).when(userRepository).findByEmail(request.email());

    //when, then
    Assertions.assertThrows(
      EmailDuplicated.class, () -> this.userService.checkEmail(request.email()));
  }

  @Test
  @DisplayName("회원가입 시 닉네임 중복은 예외가 발생한다.")
  public void duplicatedNickNameTest() {
    //given
    User user = getUser();
    UserSignUp.Request request = Request.builder()
      .nickName(user.getNickname())
      .build();

    doReturn(Optional.of(user)).when(userRepository).findByNickname(request.nickName());

    //when, then
    Assertions.assertThrows(
      NickNameDuplicated.class, () -> this.userService.checkNickName(request.nickName()));
  }

}

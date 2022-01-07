package com.prgrms.monthsub.module.part.user.app;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.dto.UserEdit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class UserAssembleTest {
  private static final String FILE_KEY = "editFilename.jpg";

  @InjectMocks
  private UserAssemble userAssemble;

  @Mock
  private UserService userService;

  @Mock
  private S3Client s3Client;

  private User user;
  private MockMultipartFile file;

  @BeforeEach
  void setUp() {
    this.user = User.builder()
      .email("user@test.com")
      .username("userName")
      .password("user123")
      .nickname("nickName")
      .point(10000)
      .build();
    ReflectionTestUtils.setField(this.user, "id", 1L);

    this.file = new MockMultipartFile(
      "file",
      FILE_KEY,
      "multipart/form-data",
      "file".getBytes()
    );
  }

  @Test
  @DisplayName("유저 정보를 수정할 수 있다.")
  public void editUserTEst() {
    UserEdit.Request request = UserEdit.Request.builder()
      .nickName("editName")
      .profileIntroduce("editIntroduce")
      .build();

    when(this.userService.findById(anyLong())).thenReturn(this.user);
    when(this.s3Client.getExtension(any())).thenReturn(FILE_KEY);
    when(this.s3Client.upload(any(), any(), any())).thenReturn(FILE_KEY);

    //when
    UserEdit.Response response = userAssemble.edit(
      this.user.getId(), request, ofNullable(this.file)
    );

    //then
    assertThat(response.userId(), is(1L));
  }
}

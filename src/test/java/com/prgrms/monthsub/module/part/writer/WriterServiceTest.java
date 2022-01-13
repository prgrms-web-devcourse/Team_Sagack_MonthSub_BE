package com.prgrms.monthsub.module.part.writer;

import static java.util.Optional.ofNullable;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.prgrms.monthsub.module.part.user.app.PartService;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.WriterRepository;
import com.prgrms.monthsub.module.part.writer.app.WriterService;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WriterServiceTest {

  @InjectMocks
  private WriterService writerService;

  @Mock
  private UserService userService;

  @Mock
  private PartService partService;

  @Mock
  private WriterRepository writerRepository;

  private User user;
  private Part part;
  private Writer writer;

  @BeforeEach
  void setUp() {
    this.part = new Part();
    ReflectionTestUtils.setField(this.part, "id", 2L);
    ReflectionTestUtils.setField(this.part, "name", "AUTHOR_GROUP");

    this.user = User.builder()
      .email("dahee@test.com")
      .username("다희")
      .password("testpassword")
      .nickname("히히")
      .point(10000)
      .part(this.part)
      .build();
    ReflectionTestUtils.setField(this.user, "id", 1L);

    this.writer = Writer.builder()
      .followCount(10)
      .user(this.user)
      .build();
    ReflectionTestUtils.setField(this.writer, "id", 1L);
  }

  @Test
  @DisplayName("시리즈 첫 발행 유저는 작가 권한을 얻을 수 있다.")
  public void becameWriterTest() {
    //when
    when(this.userService.findById(1L)).thenReturn(this.user);
    when(this.partService.findByName(Part.Name.AUTHOR_GROUP.name())).thenReturn(this.part);
    this.user.changePart(this.part);
    when(this.writerRepository.save(this.writer)).thenReturn(this.writer);
    when(this.writerRepository.findByUserId(1L)).thenReturn(ofNullable(this.writer));

    Writer newWriter = this.writerService.findByUserId(1L);

    //then
    assertThat(this.userService.findById(
      newWriter.getUser().getId()).getPart().getId(),
      equalTo(2L)
    );

    assertThat(this.userService.findById(
        newWriter.getUser().getId()).getPart().getName(),
      equalTo("AUTHOR_GROUP")
    );
  }

  @Test
  @DisplayName("시리즈 발행했던 유저는 이미 작가이다.")
  public void alreadyWriterTest() {
    //when
    when(this.userService.findById(1L)).thenReturn(this.user);
    when(this.writerRepository.save(this.writer)).thenReturn(this.writer);
    when(this.writerRepository.findByUserId(1L)).thenReturn(ofNullable(this.writer));

    Writer newWriter = this.writerService.findByUserId(1L);

    //then
    assertThat(this.userService.findById(
        newWriter.getUser().getId()).getPart().getId(),
      equalTo(2L)
    );

    assertThat(this.userService.findById(
        newWriter.getUser().getId()).getPart().getName(),
      equalTo("AUTHOR_GROUP")
    );
  }

}

package com.prgrms.monthsub.module.part.writer.app;

import static com.prgrms.monthsub.module.part.writer.domain.Writer.DEFAULT_FOLLOW_COUNT;

import com.prgrms.monthsub.module.part.user.app.PartService;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterNotFound;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WriterService implements WriterProvider {

  private final WriterRepository writerRepository;
  private final PartService partService;
  private final UserService userService;

  public WriterService(
    WriterRepository writerRepository,
    PartService partService,
    UserService userService
  ) {
    this.writerRepository = writerRepository;
    this.partService = partService;
    this.userService = userService;
  }

  @Transactional
  @Override
  public Writer findByUserId(Long userId) {
    return this.writerRepository
      .findByUserId(userId)
      .orElseGet(() -> this.getWriterAndChangeUserPart(userId));
  }

  @Transactional(readOnly = true)
  public Writer findWriterByUserId(Long userId) {
    return this.writerRepository
      .findByUserId(userId)
      .orElseThrow(() -> new WriterNotFound("id=" + userId));
  }

  @Transactional(readOnly = true)
  public Optional<Writer> findWriterObjectByUserId(Long userId) {
    return this.writerRepository
      .findByUserId(userId);
  }

  private Writer getWriterAndChangeUserPart(Long userId) {
    User user = this.userService.findById(userId);

    //TODO : PART NAME ENUM으로 관리 필요.
    Part part = this.partService.findByName("AUTHOR_GROUP");
    user.changePart(part);

    Writer entity = Writer.builder()
      .followCount(DEFAULT_FOLLOW_COUNT)
      .user(user)
      .build();

    this.writerRepository.save(entity);

    return entity;
  }

}

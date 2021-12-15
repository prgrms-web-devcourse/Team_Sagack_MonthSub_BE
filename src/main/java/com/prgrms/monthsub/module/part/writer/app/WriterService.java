package com.prgrms.monthsub.module.part.writer.app;

import static com.prgrms.monthsub.module.part.writer.domain.Writer.DEFAULT_FOLLOW_COUNT;

import com.prgrms.monthsub.module.part.user.app.PartService;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterNotFound;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
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

  @Override
  public Writer findById(Long id) {
    return this.writerRepository
      .findById(id)
      .orElseThrow(() -> new WriterNotFound("id=" + id));
  }

  @Transactional
  @Override
  public Writer findByUserId(Long userId) {
    return this.writerRepository
      .findByUserId(userId)
      .orElseGet(() -> this.becameWriter(userId));
  }

  public Optional<Writer> findByUserIdOrEmpty(Long userId) {
    return this.writerRepository.findByUserId(userId);
  }

  public List<Writer> findAll(Pageable pageable) {
    return this.writerRepository
      .findAll(pageable)
      .getContent();
  }

  private Writer becameWriter(Long userId) {
    User user = this.userService.findById(userId);
    Part part = this.partService.findByName(Part.Name.AUTHOR_GROUP.name());

    user.changePart(part);

    Writer entity = Writer.builder()
      .followCount(DEFAULT_FOLLOW_COUNT)
      .user(user)
      .build();

    this.writerRepository.save(entity);

    return entity;
  }

}

package com.prgrms.monthsub.module.part.writer.app;

import static com.prgrms.monthsub.module.part.writer.domain.Writer.DEFAULT_FOLLOW_COUNT;

import com.prgrms.monthsub.module.part.user.app.PartService;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.app.provider.WriterProvider;
import com.prgrms.monthsub.module.part.writer.converter.WriterConverter;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterNotFound;
import com.prgrms.monthsub.module.part.writer.dto.WriterList;
import com.prgrms.monthsub.module.part.writer.dto.WriterList.Response;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WriterService implements WriterProvider {

  private final int PAGE_NUM = 0;
  private final int PAGE_WRITER_NUM = 10;
  private final WriterRepository writerRepository;
  private final PartService partService;
  private final UserService userService;
  private final WriterConverter writerConverter;

  public WriterService(
    WriterRepository writerRepository,
    PartService partService,
    UserService userService,
    WriterConverter writerConverter
  ) {
    this.writerRepository = writerRepository;
    this.partService = partService;
    this.userService = userService;
    this.writerConverter = writerConverter;
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

  @Override
  public Optional<Writer> findByUserIdOrEmpty(Long userId) {
    return this.writerRepository.findByUserId(userId);
  }

  public WriterList.Response findAll() {
    return new Response(this.writerRepository.findAll(
        PageRequest.of(PAGE_NUM, PAGE_WRITER_NUM, Sort.by(Direction.DESC, "followCount"))
      )
      .getContent()
      .stream()
      .map(writerConverter::writerToWriterRes).collect(
        Collectors.toList()
      )
    );
  }

  private Writer becameWriter(Long userId) {
    User user = this.userService.findById(userId);
    Part part = this.partService.findByName(Part.Name.AUTHOR_GROUP.name());

    user.changePart(part);

    Writer writer = Writer.builder()
      .followCount(DEFAULT_FOLLOW_COUNT)
      .user(user)
      .build();

    this.writerRepository.save(writer);

    return writer;
  }

}

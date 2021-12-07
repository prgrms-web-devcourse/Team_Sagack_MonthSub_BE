package com.prgrms.monthsub.module.part.writer.app;

import com.prgrms.monthsub.module.part.user.app.PartService;
import com.prgrms.monthsub.module.part.user.app.UserService;
import com.prgrms.monthsub.module.part.user.app.inferface.WriterProvider;
import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.writer.domain.Writer;
import com.prgrms.monthsub.module.part.writer.domain.exception.WriterException.WriterNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WriterService implements WriterProvider {

    private static final int DEFAULT_FOLLOW_COUNT = 0;

    private final WriterRepository writerRepository;

    private final PartService partService;

    private final UserService userService;

    public WriterService(WriterRepository writerRepository,
        PartService partService, UserService userService) {
        this.writerRepository = writerRepository;
        this.partService = partService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Writer findByUserId(Long userId) {
        return writerRepository.findByUserId(userId).orElseGet(() ->
            this.getWriterAndChangeUserPart(userId));
    }

    @Transactional(readOnly = true)
    public Writer findWriterByUserId(Long userId) {
        return writerRepository.findByUserId(userId)
            .orElseThrow(() -> new WriterNotFound("id=" + userId));
    }

    private Writer getWriterAndChangeUserPart(Long userId) {
        User user = userService.findByUserId(userId);

        //TODO : PART NAME ENUM으로 관리 필요.
        Part part = partService.findByName("AUTHOR_GROUP");
        user.changePart(part);

        Writer entity = Writer.builder()
            .followCount(DEFAULT_FOLLOW_COUNT)
            .user(user)
            .build();

        writerRepository.save(entity);
        return entity;
    }

}

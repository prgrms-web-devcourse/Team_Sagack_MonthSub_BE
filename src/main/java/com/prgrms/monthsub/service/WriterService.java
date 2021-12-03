package com.prgrms.monthsub.service;

import com.prgrms.monthsub.domain.Part;
import com.prgrms.monthsub.domain.User;
import com.prgrms.monthsub.domain.Writer;
import com.prgrms.monthsub.repository.WriterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WriterService {

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
    public Writer findByUserId(Long userId) {
        return writerRepository.findByUserId(userId).orElseGet(() ->
            this.getWriterAndChangeUserPart(userId));
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

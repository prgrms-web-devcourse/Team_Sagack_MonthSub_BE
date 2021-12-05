package com.prgrms.monthsub.service;

import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.domain.part.PartException.PartNotFound;
import com.prgrms.monthsub.domain.Part;
import com.prgrms.monthsub.repository.PartRepository;
import org.springframework.stereotype.Service;

@Service
public class PartService {

    private final PartRepository writerRepository;

    public PartService(PartRepository writerRepository) {this.writerRepository = writerRepository;}

    public Part findByName(String name) {
        return writerRepository.findByName(name)
            .orElseThrow(() -> new PartNotFound("name=" + name));
    }

}

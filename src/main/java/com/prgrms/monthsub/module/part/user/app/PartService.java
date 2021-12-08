package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.exception.PartException.PartNotFound;
import org.springframework.stereotype.Service;

@Service
public class PartService {

  private final PartRepository writerRepository;

  public PartService(PartRepository writerRepository) {
    this.writerRepository = writerRepository;
  }

  public Part findByName(String name) {
    return this.writerRepository
      .findByName(name)
      .orElseThrow(() -> new PartNotFound("name=" + name));
  }

}

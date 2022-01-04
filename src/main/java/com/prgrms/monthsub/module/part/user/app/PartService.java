package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.module.part.user.domain.Part;
import com.prgrms.monthsub.module.part.user.domain.exception.PartException.PartNotFound;
import org.springframework.stereotype.Service;

@Service
public class PartService {

  private final PartRepository partRepository;

  public PartService(PartRepository partRepository) {
    this.partRepository = partRepository;
  }

  public Part findByName(String name) {
    return this.partRepository
      .findByName(name)
      .orElseThrow(() -> new PartNotFound("name=" + name));
  }

}

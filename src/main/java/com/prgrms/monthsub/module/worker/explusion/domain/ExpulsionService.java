package com.prgrms.monthsub.module.worker.explusion.domain;

import org.springframework.stereotype.Service;

@Service
public class ExpulsionService {
  private final ExpulsionRepository expulsionRepository;

  public ExpulsionService(
    ExpulsionRepository expulsionRepository
  ) {this.expulsionRepository = expulsionRepository;}

  public Expulsion save(Expulsion expulsion) {
    return expulsionRepository.save(expulsion);
  }
}

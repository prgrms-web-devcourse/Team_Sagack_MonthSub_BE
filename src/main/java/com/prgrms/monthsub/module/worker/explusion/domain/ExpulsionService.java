package com.prgrms.monthsub.module.worker.explusion.domain;

import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.Status;
import org.springframework.stereotype.Service;

@Service
public class ExpulsionService {
  private final ExpulsionRepository expulsionRepository;

  public ExpulsionService(
    ExpulsionRepository expulsionRepository
  ) {this.expulsionRepository = expulsionRepository;}

  public void save(
    Long userId,
    String originalKey,
    Status status,
    FileCategory fileCategory
  ) {
//    Expulsion expulsion = Expulsion.builder()
//      .userId(userId)
//      .fileKey(originalKey)
//      .status(status)
//      .fileCategory(fileCategory)
//      .hardDeleteDate(LocalDateTime.now())
//      .build();
//    this.expulsionRepository.save(expulsion);
  }

}

package com.prgrms.monthsub.module.worker.explusion.domain;

import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageName;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.ExpulsionImageStatus;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class ExpulsionService {
  private final ExpulsionRepository expulsionRepository;

  public ExpulsionService(
    ExpulsionRepository expulsionRepository
  ) {this.expulsionRepository = expulsionRepository;}

  public void save(
    Long userId,
    String originalThumbnailKey,
    ExpulsionImageStatus expulsionImageStatus,
    ExpulsionImageName expulsionImageName
  ) {
    Expulsion expulsion = Expulsion.builder()
      .userId(userId)
      .imageKey(originalThumbnailKey)
      .expulsionImageStatus(expulsionImageStatus)
      .expulsionImageName(expulsionImageName)
      .hardDeleteDate(LocalDateTime.now())
      .build();
    this.expulsionRepository.save(expulsion);
  }

}

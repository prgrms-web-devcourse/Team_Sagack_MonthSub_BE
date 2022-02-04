package com.prgrms.monthsub.module.worker.expulsion.app;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.worker.expulsion.app.provider.ExpulsionProvider;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.DomainType;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.FileType;
import com.prgrms.monthsub.module.worker.expulsion.domain.Expulsion.Status;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpulsionService implements ExpulsionProvider {

  private final int CHUNK_SIZE = 100;
  private final ExpulsionRepository expulsionRepository;
  private final S3Client s3Client;

  public ExpulsionService(
    ExpulsionRepository expulsionRepository,
    S3Client s3Client
  ) {
    this.expulsionRepository = expulsionRepository;
    this.s3Client = s3Client;
  }

  @Override
  public void save(
    Long domainId,
    Long userId,
    String originalKey,
    Status status,
    DomainType domainType,
    FileCategory fileCategory,
    FileType fileType
  ) {
    Expulsion expulsion = Expulsion.builder()
      .domainId(domainId)
      .userId(userId)
      .fileKey(originalKey)
      .status(status)
      .domainType(domainType)
      .fileCategory(fileCategory)
      .fileType(fileType)
      .build();

    this.expulsionRepository.save(expulsion);
  }

  @Transactional
  public void deleteBulk(Bucket bucket) {
    long totalCount = this.expulsionRepository.countByStatus(Status.CREATED);
    long loopCount = (long) Math.ceil(totalCount / (double) CHUNK_SIZE);

    for (int i = 0; i < loopCount; i++) {
      List<Expulsion> expulsions = this.expulsionRepository
        .findAllByStatus(
          Status.CREATED,
          PageRequest.of(
            0,
            CHUNK_SIZE,
            Sort.by(Direction.ASC, "softDeleteDate", "id")
          )
        );

      this.s3Client.deleteKeys(
        bucket,
        expulsions
          .stream()
          .map(Expulsion::getFileKey)
          .toList()
      );

      expulsions.forEach(Expulsion::hardDelete);
    }
  }

}

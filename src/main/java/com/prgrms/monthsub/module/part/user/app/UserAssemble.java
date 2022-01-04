package com.prgrms.monthsub.module.part.user.app;

import com.prgrms.monthsub.common.s3.S3Client;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.module.part.user.domain.User;
import com.prgrms.monthsub.module.part.user.dto.UserEdit;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.DomainType;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileCategory;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.FileType;
import com.prgrms.monthsub.module.worker.explusion.domain.Expulsion.Status;
import com.prgrms.monthsub.module.worker.explusion.domain.ExpulsionService;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
public class UserAssemble {
  private final UserService userService;
  private final ExpulsionService expulsionService;
  private final S3Client s3Client;

  public UserAssemble(
    UserService userService,
    ExpulsionService expulsionService,
    S3Client s3Client
  ) {
    this.userService = userService;
    this.expulsionService = expulsionService;
    this.s3Client = s3Client;
  }

  @Transactional
  public UserEdit.Response edit(
    Long id,
    UserEdit.Request request,
    Optional<MultipartFile> image
  ) {
    this.userService.checkNickName(request.nickName(), id);
    User user = this.userService.findById(id);

    image.map(multipartFile -> this.uploadProfileImage(multipartFile, user));
    user.editUser(request.nickName(), request.profileIntroduce());

    return new UserEdit.Response(user.getId());
  }

  @Transactional
  public String uploadProfileImage(
    MultipartFile image,
    User user
  ) {
    if (image.isEmpty()) {
      return null;
    }

    String key =
      "users/" + user.getId().toString()
        + "/profile/"
        + UUID.randomUUID()
        + this.s3Client.getExtension(image);

    String profileKey = this.s3Client.upload(
      Bucket.IMAGE,
      image,
      key
    );

    String originalProfileKey = user.getProfileKey();

    if (originalProfileKey != null) {
      expulsionService.save(
        user.getId(),
        user.getId(),
        originalProfileKey,
        Status.CREATED,
        DomainType.USER,
        FileCategory.USER_PROFILE,
        FileType.IMAGE
      );
    }

    user.changeProfileKey(profileKey);

    return key;
  }


}

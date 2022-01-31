package com.prgrms.monthsub.module.worker.expulsion.app;

import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expulsion/execute")
public class ExpulsionController {

  private final ExpulsionService expulsionService;

  public ExpulsionController(ExpulsionService expulsionService) {
    this.expulsionService = expulsionService;
  }

  @PostMapping
  @Operation(summary = "S3 expulsion 벌크 실행")
  @Tag(name = "[어드민]")
  public void deleteBulk(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestParam(value = "bucket", required = true) String bucket
  ) {
    this.expulsionService.deleteBulk(Bucket.of(bucket));
  }

}

package com.prgrms.monthsub.module.worker.explusion.domain;

import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import com.prgrms.monthsub.common.security.jwt.JwtAuthentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/expulsion")
public class ExpulsionController {

  private final ExpulsionService expulsionService;

  public ExpulsionController(ExpulsionService expulsionService) {
    this.expulsionService = expulsionService;
  }

  @DeleteMapping
  @Operation(summary = "어드민 API")
  @Tag(name = "[화면]-없음")
  public void deleteBulk(
    @AuthenticationPrincipal JwtAuthentication authentication,
    @RequestParam(value = "bucket", required = true) String bucket
  ) {
    this.expulsionService.deleteBulk(Bucket.of(bucket));
  }

}

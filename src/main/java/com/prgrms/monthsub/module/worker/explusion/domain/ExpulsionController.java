package com.prgrms.monthsub.module.worker.explusion.domain;

import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import io.swagger.v3.oas.annotations.Operation;
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
  @Operation(hidden = true)
  public void deleteBulk(@RequestParam(value = "bucket", required = true) String bucket) {
    this.expulsionService.deleteBulk(Bucket.of(bucket));
  }

}

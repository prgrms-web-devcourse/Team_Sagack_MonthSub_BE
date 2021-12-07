package com.prgrms.monthsub.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3 {
  private String imageBucket;
  private String videoBucket;
  private String domain;

  public enum Bucket {
    IMAGE, VIDEO;
  }

  public String getBucket(Bucket bucket) {
    return switch (bucket) {
      case IMAGE -> this.imageBucket;
      case VIDEO -> this.videoBucket;
    };
  }
}


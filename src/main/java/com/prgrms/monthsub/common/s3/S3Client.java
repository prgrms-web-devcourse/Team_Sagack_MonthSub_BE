package com.prgrms.monthsub.common.s3;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.prgrms.monthsub.common.exception.global.S3UploaderException.DeleteError;
import com.prgrms.monthsub.common.exception.global.S3UploaderException.ImageExtensionNotMatch;
import com.prgrms.monthsub.common.exception.global.S3UploaderException.ReadyToUploadError;
import com.prgrms.monthsub.common.exception.global.S3UploaderException.UploadError;
import com.prgrms.monthsub.common.s3.config.AWS;
import com.prgrms.monthsub.common.s3.config.S3;
import com.prgrms.monthsub.common.s3.config.S3.Bucket;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Client {

  private static final List<String> imageExtensions = Arrays.asList("jpeg", "png", "jpg", "gif");

  private final AWS aws;
  private final S3 s3;

  private final Logger log = LoggerFactory.getLogger(getClass());
  private AmazonS3 s3Client;

  public S3Client(
    AWS aws,
    S3 s3
  ) {
    this.aws = aws;
    this.s3 = s3;
  }

  @PostConstruct
  public void setS3Client() {
    AWSCredentials credentials = new BasicAWSCredentials(
      aws.getCredentials().getAccessKey(),
      aws.getCredentials().getSecretKey()
    );

    s3Client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(aws.getRegion())
      .build();
  }

  public String getExtension(MultipartFile file) {
    return Objects.requireNonNull(file.getOriginalFilename())
      .substring(file.getOriginalFilename().indexOf("."));
  }

  public void deleteKeys(
    Bucket bucket,
    List<String> keys
  ) {
    try {
      List<KeyVersion> deleteKeys = keys.stream()
        .map(KeyVersion::new)
        .collect(Collectors.toList());

      s3Client.deleteObjects(
        new DeleteObjectsRequest(this.s3.getBucket(bucket))
          .withKeys(deleteKeys)
          .withQuiet(false)
      );
    } catch (SdkClientException e) {
      String message = "S3 file delete failed";

      log.error(message);
      e.printStackTrace();
      throw new DeleteError(message);
    }
  }

  public String upload(
    Bucket bucket,
    MultipartFile file,
    String key
  ) {
    switch (bucket) {
      case IMAGE -> this.validImageFile(file);
      case VIDEO -> this.validVideoFile(file);
    }

    ObjectMetadata objectMetadata = new ObjectMetadata();
    byte[] bytes;
    try {
      bytes = IOUtils.toByteArray(file.getInputStream());
    } catch (IOException e) {
      String message = "File=" + file.getOriginalFilename();

      log.error(message);
      e.printStackTrace();
      throw new ReadyToUploadError(message);
    }
    objectMetadata.setContentType(file.getContentType());
    objectMetadata.setContentLength(bytes.length);

    try {
      s3Client.putObject(
        new PutObjectRequest(
          this.s3.getBucket(bucket),
          key,
          file.getInputStream(),
          objectMetadata
        ).withCannedAcl(CannedAccessControlList.PublicRead)
      );
    } catch (IOException e) {
      String message = "File=" + file.getOriginalFilename();

      log.error(message);
      e.printStackTrace();
      throw new UploadError(message);
    }

    return key;
  }

  private void validImageFile(MultipartFile file) {
    String[] fileInfo = Objects.requireNonNull(file.getContentType()).split("/");

    boolean isImage = Arrays
      .stream(fileInfo)
      .anyMatch(imageExtensions::contains);

    if (!isImage) {
      throw new ImageExtensionNotMatch();
    }
  }

  private void validVideoFile(MultipartFile file) {

  }

}

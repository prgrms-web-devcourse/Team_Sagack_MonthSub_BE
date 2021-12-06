package com.prgrms.monthsub.common.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.IOUtils;
import com.prgrms.monthsub.common.config.AWS;
import com.prgrms.monthsub.common.config.S3;
import com.prgrms.monthsub.common.config.S3.Bucket;
import com.prgrms.monthsub.common.exception.global.S3UploaderException.ImageExtensionNotMatch;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Uploader {
//  private static final String PROFILE = "profile";
//  private static final String THUMBNAIL = "thumbnail";

    private AmazonS3 s3Client;

    private final AWS aws;

    private final S3 s3;

    public S3Uploader(AWS aws, S3 s3) {
        this.aws = aws;
        this.s3 = s3;
    }

    @PostConstruct
    public void setS3Client() {

        AWSCredentials credentials = new BasicAWSCredentials(
            aws.getCredentials().getAccessKey(), aws.getCredentials().getSecretKey());

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

    public String upload(Bucket bucket, MultipartFile file, String key)
        throws IOException {
        // 도메인 마다 null 허용 정책 다름.
        if (file.isEmpty()) {
            return null;
        }

        fileExtensionCheck(file.getContentType().split("/"));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(bytes.length);

        PutObjectResult uploadResponse = s3Client.putObject(
            new PutObjectRequest(
                this.s3.getBucket(bucket),
                key,
                file.getInputStream(),
                objectMetadata
            ).withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return key;
    }

    private void fileExtensionCheck(String[] fileInfo) {
        boolean isImage = Arrays.stream(fileInfo)
            .anyMatch(
                info -> (info.contains("jpeg") || info.contains("png") || (info.contains("jpg"))));
        if (!isImage) {
            throw new ImageExtensionNotMatch();
        }
    }

}

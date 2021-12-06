package com.prgrms.monthsub.common.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.prgrms.monthsub.common.exception.global.S3UploaderException.ImageExtensionNotMatch;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3Uploader {

    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        s3Client = AmazonS3ClientBuilder
            .standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(region)
            .build();
    }

    public String upload(MultipartFile image, String domainClassName, Long userId, String imagePurpose)
        throws IOException {
        // 도메인 마다 null 허용 정책 다름.
        if (image.isEmpty()) {
            return null;
        }

        fileExtensionCheck(image.getContentType().split("/"));

        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(image.getInputStream());
        objectMetadata.setContentType(image.getContentType());
        objectMetadata.setContentLength(bytes.length);

        String fileName =
            domainClassName.toLowerCase() + "/" + userId.toString() + "/" + imagePurpose + "/"
                + UUID.randomUUID();

        s3Client.putObject(
            new PutObjectRequest(
                bucket, fileName, image.getInputStream(), objectMetadata)
                .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        return s3Client
            .getUrl(bucket, fileName)
            .toString();
    }

    private void fileExtensionCheck(String[] fileInfo) {
        boolean isImage = Arrays.stream(fileInfo)
            .anyMatch(info -> (info.contains("jpeg") || info.contains("png") || (info.contains("jpg"))));
        if (!isImage) {
            throw new ImageExtensionNotMatch();
        }
    }

}

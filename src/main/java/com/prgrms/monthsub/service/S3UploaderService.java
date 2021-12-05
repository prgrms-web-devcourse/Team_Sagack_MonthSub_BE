package com.prgrms.monthsub.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.prgrms.monthsub.common.error.ErrorCodes;
import com.prgrms.monthsub.common.error.exception.global.BusinessException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3UploaderService {

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

    public String upload(MultipartFile file, String dirName) throws IOException {
        fileExtensionCheck(file.getContentType().split("/"));

        InputStream uploadFile = file.getInputStream();
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        byte[] bytes = IOUtils.toByteArray(file.getInputStream());
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(bytes.length);

        s3Client.putObject(
            new PutObjectRequest(bucket, fileName, file.getInputStream(), objectMetadata)
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
            throw new BusinessException(ErrorCodes.INVALID_UPLOAD_FILE_TYPE());
        }
    }

}

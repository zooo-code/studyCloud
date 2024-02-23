package com.cloudstudy.cloud.ncloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Configuration
@EnableConfigurationProperties(S3Properties.class)
public class BucketConfig {

    private final String endPoint = "https://kr.object.ncloudstorage.com";
    private final String regionName = "kr-standard";

    private final S3Properties properties;

    public BucketConfig(S3Properties properties) {
        this.properties = properties;
    }

    @Bean
    public S3Client s3Client() {
        S3Client s3 = S3Client.builder()
                .endpointOverride(createURI(endPoint))
                .region(Region.of(regionName))
                .credentialsProvider(() -> AwsBasicCredentials.create(properties.getAccessKey(), properties.getSecretKey()))
                .build();

        // 버킷 생성
        createBucket(s3, properties.getBucketName());

        return s3;
    }

    private void createBucket(S3Client s3, String bucketName) {
        try {
            // 버킷이 존재하는지 확인
            HeadBucketResponse headBucketResponse = s3.headBucket(HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build());
            if (headBucketResponse.sdkHttpResponse().isSuccessful()) {
                log.info("Bucket {} already exists.", bucketName);
            } else {
                // 버킷 생성
                s3.createBucket(CreateBucketRequest.builder()
                        .bucket(bucketName)
                        .build());
                log.info("Bucket {} has been created.", bucketName);
            }
        } catch (S3Exception e) {
            // 서비스 요청이 실패한 경우
            e.printStackTrace();
        }
    }

    private URI createURI(String endpoint) {
        try {
            return new URI(endpoint);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.cloudstudy.cloud.ncloud;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties("spring.s3")
public class S3Properties {


    private String accessKey;

    private String secretKey;

    private String bucketName;

    public S3Properties(String accessKey, String secretKey, String bucketName) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.bucketName = bucketName;
    }
}

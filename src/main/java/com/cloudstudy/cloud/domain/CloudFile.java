package com.cloudstudy.cloud.domain;


import lombok.Builder;
import lombok.Data;

@Data
public class CloudFile {

    private Long id;
    private Long cloudId;
    private String uploadFileName;
    private String storeFileName;

    @Builder
    public CloudFile(Long id, Long cloudId, String uploadFileName, String storeFileName) {
        this.id = id;
        this.cloudId = cloudId;
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}

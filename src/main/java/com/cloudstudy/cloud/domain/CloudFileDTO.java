package com.cloudstudy.cloud.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class CloudFileDTO {

    private Long cloudId;
    private String originalFileName;
    private String uploadFileName;
    private String uploadFilePath;
    private String uploadFileUrl;
    @Builder
    public CloudFileDTO(Long cloudId, String originalFileName, String uploadFileName, String uploadFilePath, String uploadFileUrl) {
        this.cloudId = cloudId;
        this.originalFileName = originalFileName;
        this.uploadFileName = uploadFileName;
        this.uploadFilePath = uploadFilePath;
        this.uploadFileUrl = uploadFileUrl;
    }

}

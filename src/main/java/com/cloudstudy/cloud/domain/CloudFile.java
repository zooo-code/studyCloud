package com.cloudstudy.cloud.domain;


import lombok.Builder;
import lombok.Data;


/**
 * 파일에 대한 메타 데이터를 저장하기 위한 도메인 입니다.
 */
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

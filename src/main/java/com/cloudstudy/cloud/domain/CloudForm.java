package com.cloudstudy.cloud.domain;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class CloudForm {

    private Long cloudId;
    private String name;
    private List<MultipartFile> imageFiles;
}

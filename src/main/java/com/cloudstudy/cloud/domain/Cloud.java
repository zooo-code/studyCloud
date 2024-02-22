package com.cloudstudy.cloud.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class Cloud {


    private Long id;

    private String name;

    private List<CloudFile> CloudFiles;
    public Cloud() {
    }

    @Builder
    public Cloud(Long id, String name, List<CloudFile> CloudFiles) {
        this.id = id;
        this.name = name;
        this.CloudFiles = CloudFiles;
    }
}

package com.cloudstudy.cloud.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Cloud {


    private Long id;

    private String name;

    @Builder
    public Cloud(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

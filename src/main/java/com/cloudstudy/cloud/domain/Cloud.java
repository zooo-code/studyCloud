package com.cloudstudy.cloud.domain;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
public class Cloud {


    private Long id;

    private String name;

    public Cloud() {
    }

    @Builder
    public Cloud(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

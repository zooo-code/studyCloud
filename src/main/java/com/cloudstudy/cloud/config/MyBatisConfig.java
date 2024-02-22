package com.cloudstudy.cloud.config;

import com.cloudstudy.cloud.repository.CloudFileMapper;
import com.cloudstudy.cloud.repository.CloudFileRepository;
import com.cloudstudy.cloud.repository.CloudMapper;
import com.cloudstudy.cloud.repository.CloudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final CloudMapper cloudMapper;
    private final CloudFileMapper cloudFileMapper;

    @Bean
    public CloudRepository cloudRepository(){
        return new CloudRepository(cloudMapper);
    }

    @Bean
    public CloudFileRepository cloudFileRepository(){
        return new CloudFileRepository(cloudFileMapper);
    }

}

package com.cloudstudy.cloud.config;

import com.cloudstudy.cloud.repository.CloudMapper;
import com.cloudstudy.cloud.repository.CloudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MyBatisConfig {

    private final CloudMapper cloudMapper;

    @Bean
    public CloudRepository cloudRepository(){
        return new CloudRepository(cloudMapper);
    }
}

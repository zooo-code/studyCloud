package com.cloudstudy.cloud.config;

import com.cloudstudy.cloud.repository.CloudFileMapper;
import com.cloudstudy.cloud.repository.CloudFileRepository;
import com.cloudstudy.cloud.repository.CloudMapper;
import com.cloudstudy.cloud.repository.CloudRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 마이바티스에 대한 설정 입니다.
 * 스프링을 통해 Bean 으로 관리하기 위해 의존성 주입을 진행합니다.
 */
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

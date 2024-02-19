package com.cloudstudy.cloud.repository;


import com.cloudstudy.cloud.domain.Cloud;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@RequiredArgsConstructor
@Repository
public class CloudRepository  {

    private final CloudMapper cloudMapper;


    public Cloud save(Cloud cloud) {
        cloudMapper.save(cloud);
        return cloud;
    }

    public Cloud findById(Long id) {
        return cloudMapper.findById(id);
    }



    public List<Cloud> findAll() {
        return cloudMapper.findAll();
    }

    public void update(Long cloudId, Cloud updateParam) {
        cloudMapper.update(cloudId,updateParam);
    }
}

package com.cloudstudy.cloud.repository;


import com.cloudstudy.cloud.domain.Cloud;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CloudRepository {


    private static final Map<Long, Cloud> store = new HashMap<>(); //static
    private static long sequence = 0L; //static

    public Cloud save(Cloud cloud) {
        cloud.setId(++sequence);
        store.put(cloud.getId(), cloud);
        return cloud;
    }

    public Cloud findById(Long id) {
        return store.get(id);
    }

    public List<Cloud> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long cloudId, Cloud updateParam) {
        Cloud findItem = findById(cloudId);
        findItem.setName(updateParam.getName());
    }

    public void clearStore() {
        store.clear();
    }
}

package com.cloudstudy.cloud.repository;

import com.cloudstudy.cloud.domain.Cloud;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CloudMapper {

    void save(Cloud cloud);

    void update(@Param("cloudId") Long cloudId, @Param("updateParam") Cloud updateParam);

    Cloud findById(Long id);

    List<Cloud> findAll();
}

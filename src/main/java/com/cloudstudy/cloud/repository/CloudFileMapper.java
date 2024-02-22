package com.cloudstudy.cloud.repository;


import com.cloudstudy.cloud.domain.CloudFile;
import com.cloudstudy.cloud.domain.CloudFileDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CloudFileMapper {

    void save(CloudFileDTO cloudFileDTO);

    void saveAll(List<CloudFileDTO> cloudFileDTOList);

    List<CloudFile> findByIds(Long cloudId);
}

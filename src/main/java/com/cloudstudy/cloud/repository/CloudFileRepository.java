package com.cloudstudy.cloud.repository;

import com.cloudstudy.cloud.domain.CloudFile;
import com.cloudstudy.cloud.domain.CloudFileDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CloudFileRepository{

    private final CloudFileMapper cloudFileMapper;
    public void save(CloudFileDTO cloudFileDTO) {
        cloudFileMapper.save(cloudFileDTO);
    }


    public void savaAll(List<CloudFileDTO> cloudFileDTOList){
        cloudFileMapper.saveAll(cloudFileDTOList);
    }

    public List<CloudFile> findByIds(Long cloudId){
        log.info("CloudId {}" , cloudId);
        return cloudFileMapper.findByIds(cloudId);
    }
}

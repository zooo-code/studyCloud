package com.cloudstudy.cloud.ncloud;

import com.cloudstudy.cloud.domain.CloudFileDTO;
import com.cloudstudy.cloud.repository.CloudFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Client amazonS3Client;

    private final S3Properties properties;

    /**
     * 파일의 UUID 를 만들어 냅니다.
     * @param fileName 파일 이름에 있는 확장자를 추출하기 위한 파라미터 입니다.
     * @return
     */
    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return UUID.randomUUID() + "." + ext;
    }

    /**
     * 저장을 요청 받은 파일 리스트를 버킷에 업로드 하는 메서드 입니다.
     * @param multipartFiles
     * @return
     */
    public List<CloudFileDTO> uploadFilesSample(List<MultipartFile> multipartFiles) {
        return uploadFiles(multipartFiles, "sample");
    }

    /**
     * 업로드 요청 받을 파일들을 하나씩 업로드 진행합니다.
     * 파일의 경로를 설정하고 s3 버킷에 파일을 업로드 합니다.
     * 마지막으로 저장된 파일들을 CloudFileDTO 에 담아 리스트로 리턴해줍니다.
     * @param multipartFiles
     * @param filePath 버킷에 있는 디렉토리 입니다.
     * @return
     */
    public List<CloudFileDTO> uploadFiles(List<MultipartFile> multipartFiles, String filePath) {
        List<CloudFileDTO> s3Files = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            String originalFileName = multipartFile.getOriginalFilename();
            String uploadFileName = getUuidFileName(originalFileName);
            String uploadFileUrl = "";

            try (InputStream inputStream = multipartFile.getInputStream()) {
                String keyName = filePath + "/" + uploadFileName;

                // S3에 파일 업로드
                PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                        .bucket(properties.getBucketName())
                        .key(keyName)
                        .contentType(multipartFile.getContentType())
                        .build();

                amazonS3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));

                // S3에 업로드한 파일 URL
                uploadFileUrl = "https://kr.object.ncloudstorage.com/" + properties.getBucketName() + "/" + keyName;

            } catch (IOException e) {
                e.printStackTrace();
            }

            s3Files.add(
                    CloudFileDTO.builder()
                            .originalFileName(originalFileName)
                            .uploadFileName(uploadFileName)
                            .uploadFilePath(filePath)
                            .uploadFileUrl(uploadFileUrl)
                            .build());
        }

        return s3Files;
    }

    /**
     * 버킷에서 해당 파일 이름을 기반으로 다운을 위한 요청을 합니다.
     * @param filename
     * @return
     */
    public InputStream downloadImage(String filename){
        String keyName = "sample" + "/" + filename;
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(properties.getBucketName())
                .key(keyName)
                .build();
        ResponseInputStream<GetObjectResponse> objectContent = amazonS3Client.getObject(getObjectRequest);

        return objectContent;
    }
}

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

    @Value("${spring.s3.bucket}")
    private String bucketName;



    private final CloudFileMapper cloudFileMapper;


    public String getUuidFileName(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        return UUID.randomUUID().toString() + "." + ext;
    }


    public List<CloudFileDTO> uploadFilesSample(List<MultipartFile> multipartFiles) {
        return uploadFiles(multipartFiles, "sample");
    }

    //projectbucket/ 빈칸/project(uploadDir,filePath)/파일이름
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
                        .bucket(bucketName)
                        .key(keyName)
                        .contentType(multipartFile.getContentType())
                        .build();

                amazonS3Client.putObject(putObjectRequest, RequestBody.fromInputStream(inputStream, multipartFile.getSize()));

                // S3에 업로드한 파일 URL
                uploadFileUrl = "https://kr.object.ncloudstorage.com/" + bucketName + "/" + keyName;

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

    public InputStream downloadImage(String filename) throws IOException {

        String keyName = "sample" + "/" + filename;
//        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
//                .bucket(bucketName)
//                .key(filename)
//                .build();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(keyName)
                .build();

        ResponseInputStream<GetObjectResponse> objectContent = amazonS3Client.getObject(getObjectRequest);

        return objectContent;
    }
}

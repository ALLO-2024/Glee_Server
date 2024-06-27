package com.allo.server.global.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class S3Service {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.folder.folderName}")
    private String folder;

    @Value("${cloud.aws.s3.url}")
    private String defaultUrl;

    @Async("imageUploadExecutor")
    public CompletableFuture<URL> uploadFile(MultipartFile multipartFile) throws IOException {

        CompletableFuture<URL> future = new CompletableFuture<>();
        System.out.println("start: " + Thread.currentThread().getId() + "Time: " + LocalTime.now());

        String fileName = multipartFile.getOriginalFilename();
        UUID fileNameUUID = UUID.randomUUID();

        //파일 형식 구하기
        String extension = Objects.requireNonNull(fileName).split("\\.")[1];
        fileName = folder + "/" + fileNameUUID + "." + extension;

        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(multipartFile.getContentType());
            metadata.setContentLength(multipartFile.getSize());

            //S3 upload
            amazonS3.putObject(new PutObjectRequest(bucket, fileName, multipartFile.getInputStream(), metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (AmazonServiceException e) {
            e.printStackTrace();
        }
        System.out.println("end: " + Thread.currentThread().getId() + "Time: " + LocalTime.now());

        //파일 주소 리턴
        future.complete(amazonS3.getUrl(bucket, fileName));
        return future;
    }

}
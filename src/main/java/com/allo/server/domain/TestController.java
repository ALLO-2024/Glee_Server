package com.allo.server.domain;

import com.allo.server.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@EnableAsync
public class TestController {
    private final S3Service s3Service;

//    @PostMapping("/upload")
//    public String upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
//        String fileName = s3Service.uploadFile(multipartFile);
//        return fileName;
//    }
}
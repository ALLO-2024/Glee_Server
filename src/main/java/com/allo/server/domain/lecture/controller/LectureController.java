package com.allo.server.domain.lecture.controller;

import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.service.LectureService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;


    @PostMapping("/save")
    public ResponseEntity<Void> saveLecture(@AuthenticationPrincipal UserDetails loginUser,
    @RequestPart (value = "LectureCreateRequest") LectureSaveRequest lectureSaveRequest,
    @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        lectureService.saveLecture(loginUser.getUsername(), lectureSaveRequest, multipartFile);
        return ResponseEntity.noContent().build();
    }

/*
    @GetMapping("/test")
    public ResponseEntity<Void> testLecture(@AuthenticationPrincipal UserDetails loginUser,
                                            @RequestPart (value = "test") String lectureSaveRequest,
                                            @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {
        String LOCAL_STORAGE_PATH = "/path/to/local/storage/";

        // 원본 파일명
        String originalFileName = multipartFile.getOriginalFilename();

        // 로컬 저장 경로에 저장할 파일 객체 생성
        File localFile = new File(LOCAL_STORAGE_PATH + originalFileName);
        localFile.getParentFile().mkdirs();

        // MultipartFile의 InputStream을 사용하여 파일을 로컬에 저장
        try (OutputStream outputStream = new FileOutputStream(localFile)) {
            byte[] bytes = multipartFile.getBytes();
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + originalFileName, e);
        }

        lectureService.requestFileToText("eng", "2", localFile);
        return ResponseEntity.noContent().build();
    }*/
}

package com.allo.server.domain.lecture.controller;

import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @GetMapping("/search/{searchContent}")
    public ResponseEntity<List<LectureSearchResponse>> searchLecture (@AuthenticationPrincipal UserDetails loginUser, @PathVariable String searchContent) {

        List<LectureSearchResponse> response = lectureService.searchLecture(loginUser.getUsername(), searchContent);
        return ResponseEntity.ok()
    }

}

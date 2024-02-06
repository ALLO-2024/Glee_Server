package com.allo.server.domain.lecture.controller;

import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.dto.response.LectureSummaryResponseDto;
import com.allo.server.domain.lecture.service.LectureService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;


    @PostMapping("/save")
    public ResponseEntity<Void> saveLecture(@AuthenticationPrincipal UserDetails loginUser,
    @RequestPart(value = "LectureCreateRequest") LectureSaveRequest lectureSaveRequest,
    @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        lectureService.saveLecture(loginUser.getUsername(), lectureSaveRequest, multipartFile);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/translate/{lectureId}")
    public ResponseEntity<Void> translateLectureContent(@PathVariable Long lectureId) {
        lectureService.requestTranslate(lectureId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/summary/{lectureId}")
    public ResponseEntity<LectureSummaryResponseDto> summaryLectureContent(@PathVariable Long lectureId) {
        LectureSummaryResponseDto responseDto = lectureService.sendRequestToGpt(lectureId);
        log.info("response: {}", responseDto);
        return ResponseEntity.noContent().build();
    }
}

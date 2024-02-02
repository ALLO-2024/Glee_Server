package com.allo.server.domain.lecture.controller;

import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.service.LectureService;
import com.allo.server.response.BaseResponse;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

import static com.allo.server.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lectures")
public class LectureController {

    private final LectureService lectureService;


    @PostMapping("/save")
    public ResponseEntity<BaseResponse> saveLecture(@AuthenticationPrincipal UserDetails loginUser,
                                                    @RequestPart(value = "LectureCreateRequest") LectureSaveRequest lectureSaveRequest,
                                                    @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        lectureService.saveLecture(loginUser.getUsername(), lectureSaveRequest, multipartFile);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @PostMapping("/translate/{lectureId}")
    public ResponseEntity<BaseResponse> translateLectureContent(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long lectureId) {
        lectureService.requestTranslate(loginUser.getUsername(), lectureId);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }
}

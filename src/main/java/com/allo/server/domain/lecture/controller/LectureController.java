package com.allo.server.domain.lecture.controller;

import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.service.LectureService;
import com.allo.server.domain.word.dto.response.WordGetResponse;
import com.allo.server.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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


    @Operation(summary = "강의 저장 API")
    @PostMapping("/save")
    public ResponseEntity<BaseResponse> saveLecture(@AuthenticationPrincipal UserDetails loginUser,
                                                    @RequestPart(value = "LectureCreateRequest") LectureSaveRequest lectureSaveRequest,
                                                    @RequestPart(value = "file", required = false) MultipartFile multipartFile) throws IOException {

        lectureService.saveLecture(loginUser.getUsername(), lectureSaveRequest, multipartFile);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @Operation(summary = "강의 번역 API")
    @PostMapping("/translate/{lectureId}")
    public ResponseEntity<BaseResponse> translateLectureContent(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long lectureId) {
        lectureService.requestTranslate(loginUser.getUsername(), lectureId);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

//    @Operation(summary = "저장된 강의 조회 API")
//    @GetMapping("/{year}/{semester}")
//    public ResponseEntity<BaseResponse<List<LectureSearchResponse>>> getLectures(@AuthenticationPrincipal UserDetails loginUser, @PathVariable int year, @PathVariable int semester) {
//
//        List<LectureSearchResponse> response = lectureService.getLecture(loginUser.getUsername(), year, semester);
//        return ResponseEntity.ok(new BaseResponse<>(response));
//    }

}

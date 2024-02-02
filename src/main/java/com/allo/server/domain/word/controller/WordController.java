package com.allo.server.domain.word.controller;

import com.allo.server.domain.word.dto.request.WordSaveRequest;
import com.allo.server.domain.word.dto.response.WordGetResponse;
import com.allo.server.domain.word.dto.response.WordSearchResponse;
import com.allo.server.domain.word.service.WordService;
import com.allo.server.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.allo.server.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/words")
public class WordController {

    private final WordService wordService;

    @Operation(summary = "단어 검색 API")
    @GetMapping("/search/{word}")
    public ResponseEntity<BaseResponse<WordSearchResponse>> searchWord(@AuthenticationPrincipal UserDetails loginUser,
                                            @PathVariable String word) throws IOException {

        WordSearchResponse response = wordService.searchWord(loginUser.getUsername(), word);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "단어 저장 API")
    @PostMapping("/save")
    public ResponseEntity<BaseResponse> saveWord(@AuthenticationPrincipal UserDetails loginUser,
                                                 @Valid @RequestPart(value = "WordSaveRequest") WordSaveRequest wordSaveRequest) throws IOException {

        wordService.saveWord(loginUser.getUsername(), wordSaveRequest);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @Operation(summary = "저장된 단어 검색 API")
    @GetMapping("/get")
    public ResponseEntity<BaseResponse<List<WordGetResponse>>> getWords(@AuthenticationPrincipal UserDetails loginUser,
                                                          @PageableDefault(page = 1) Pageable pageable) throws IOException {

        Page<WordGetResponse> response =  wordService.getWord(loginUser.getUsername(), pageable);
        return ResponseEntity.ok(new BaseResponse<>(response.getContent()));
    }
}

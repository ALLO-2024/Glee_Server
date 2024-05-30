package com.allo.server.domain.word.controller;

import com.allo.server.domain.word.dto.request.WordSaveRequest;
import com.allo.server.domain.word.dto.response.WordGetResponse;
import com.allo.server.domain.word.dto.response.WordSearchResponse;
import com.allo.server.domain.word.entity.Word;
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
                                                 @Valid @RequestBody WordSaveRequest wordSaveRequest) throws IOException {

        wordService.saveWord(loginUser.getUsername(), wordSaveRequest);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @Operation(summary = "단어 삭제 API")
    @DeleteMapping("/delete/{wordId}")
    public ResponseEntity<BaseResponse> deleteWord(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long wordId) throws IOException {

        wordService.deleteWord(loginUser.getUsername(), wordId);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }

    @Operation(summary = "저장된 모든 단어 검색 API")
    @GetMapping("/all")
    public ResponseEntity<BaseResponse<List<WordGetResponse>>> getAll(@AuthenticationPrincipal UserDetails loginUser) throws IOException {

        List<WordGetResponse> response =  wordService.getAll(loginUser.getUsername());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "저장된 단어 페이징 처리 검색 API")
    @GetMapping("/get")
    public ResponseEntity<BaseResponse<List<WordGetResponse>>> getWords(@AuthenticationPrincipal UserDetails loginUser,
                                                                        @PageableDefault(page = 1) Pageable pageable) throws IOException {

        List<WordGetResponse> response =  wordService.getWord(loginUser.getUsername(), pageable);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}

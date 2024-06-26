package com.allo.server.domain.post.controller;

import com.allo.server.domain.post.dto.request.PostSaveRequest;
import com.allo.server.domain.post.service.PostService;
import com.allo.server.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.allo.server.response.BaseResponseStatus.SUCCESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시물 저장 API")
    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> savePost(
            @AuthenticationPrincipal UserDetails loginUser,
            @RequestPart (value = "PostSaveRequest") PostSaveRequest request,
            @RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles) throws IOException {
        postService.savePost(loginUser.getUsername(), request, multipartFiles);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }
}

package com.allo.server.domain.post.controller;

import com.allo.server.domain.post.dto.request.PostSaveRequest;
import com.allo.server.domain.post.dto.response.PostGetResponse;
import com.allo.server.domain.post.dto.response.PostListGetResponse;
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

    @Operation(summary = "저장된 게시물 조회 API")
    @GetMapping("/get/{postId}")
    public ResponseEntity<BaseResponse<PostGetResponse>> getPost(@AuthenticationPrincipal UserDetails loginUser, @PathVariable Long postId) {

        PostGetResponse response = postService.getPost(loginUser.getUsername(), postId);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "저장된 게시물 목록 조회 API")
    @GetMapping("/list")
    public ResponseEntity<BaseResponse<List<PostListGetResponse>>> getPostList(@AuthenticationPrincipal UserDetails loginUser) {

        List<PostListGetResponse> response = postService.getPostList(loginUser.getUsername());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "마이페이지 - 내가 쓴 게시물")
    @GetMapping("/mypage/list")
    public ResponseEntity<BaseResponse<List<PostListGetResponse>>> getMyPostList(@AuthenticationPrincipal UserDetails loginUser) {

        List<PostListGetResponse> response = postService.getMyPostList(loginUser.getUsername());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @Operation(summary = "마이페이지 - 관심 게시물")
    @GetMapping("/like/list")
    public ResponseEntity<BaseResponse<List<PostListGetResponse>>> getLikePostList(@AuthenticationPrincipal UserDetails loginUser) {

        List<PostListGetResponse> response = postService.getLikePostList(loginUser.getUsername());
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}

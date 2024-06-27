package com.allo.server.domain.comment.controller;

import com.allo.server.domain.comment.dto.request.CommentSaveRequest;
import com.allo.server.domain.comment.service.CommentService;
import com.allo.server.domain.post.dto.request.PostSaveRequest;
import com.allo.server.domain.post.service.PostService;
import com.allo.server.domain.word.dto.request.WordSaveRequest;
import com.allo.server.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
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
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성 API")
    @PostMapping("/save")
    public ResponseEntity<BaseResponse> saveWord(@AuthenticationPrincipal UserDetails loginUser, @Valid @RequestBody CommentSaveRequest commentSaveRequest) throws IOException {

        commentService.saveComment(loginUser.getUsername(), commentSaveRequest);
        return ResponseEntity.ok(new BaseResponse<>(SUCCESS));
    }
}

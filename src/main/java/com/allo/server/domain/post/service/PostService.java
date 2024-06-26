package com.allo.server.domain.post.service;

import com.allo.server.domain.post.dto.request.PostSaveRequest;
import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post.repository.PostRepository;
import com.allo.server.domain.post_image.entity.PostImage;
import com.allo.server.domain.post_image.repository.PostImageRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.global.s3.S3Service;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public void savePost(String email, PostSaveRequest request, List<MultipartFile> multipartFiles) throws IOException {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Post savePost = PostSaveRequest.postToEntity(userEntity, request);
        postRepository.save(savePost);

        List<CompletableFuture<URL>> futures = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFiles) {
            futures.add(s3Service.uploadFile(multipartFile));
        }

        futures.forEach(future -> {
            future.thenAccept(url -> {
                PostImage postImage = new PostImage(savePost, url.toString());
                postImageRepository.save(postImage);
            }).join();  // 이 작업은 현재 스레드를 블록하며 모든 이미지가 처리될 때까지 기다림
        });
    }

}

package com.allo.server.domain.post.service;

import com.allo.server.domain.post.dto.request.PostSaveRequest;
import com.allo.server.domain.post.dto.response.PostGetResponse;
import com.allo.server.domain.post.dto.response.PostInfoResponse;
import com.allo.server.domain.post.dto.response.PostListGetResponse;
import com.allo.server.domain.post.entity.Post;
import com.allo.server.domain.post.repository.CustomPostRepository;
import com.allo.server.domain.post.repository.PostRepository;
import com.allo.server.domain.post_image.dto.response.PostImageGetResponse;
import com.allo.server.domain.post_image.entity.PostImage;
import com.allo.server.domain.post_image.repository.CustomPostImageRepository;
import com.allo.server.domain.post_image.repository.PostImageRepository;
import com.allo.server.domain.post_like.repository.PostLikeRepository;
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

import static com.allo.server.error.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CustomPostRepository customPostRepository;
    private final PostImageRepository postImageRepository;
    private final CustomPostImageRepository customPostImageRepository;
    private final PostLikeRepository postLikeRepository;
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

    public PostGetResponse getPost(String email, Long postId) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Post post = postRepository.findById(postId).orElseThrow(() -> new BadRequestException(POST_NOT_FOUND));

        PostInfoResponse postInfoResponse = customPostRepository.getPost(userEntity.getUserId(), postId);

        List<PostImageGetResponse> postImageGetResponses = customPostImageRepository.getPostImages(postId);

        return PostGetResponse.of(postInfoResponse, postImageGetResponses);
    }

    public List<PostListGetResponse> getPostList(String email, String sortType) {
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        return customPostRepository.getPostList(sortType);
    }

}

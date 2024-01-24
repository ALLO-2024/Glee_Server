package com.allo.server.domain.lecture.service;

import com.allo.server.domain.content.entity.Content;
import com.allo.server.domain.content.repository.ContentRepository;
import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.domain.lecture.repository.LectureRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.global.s3.S3Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;

import static com.allo.server.error.ErrorCode.*;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final S3Service s3Service;
    private final static String LOCAL_STORAGE_PATH = "/path/to/local/storage/";


    @Transactional
    public void saveLecture(String email, LectureSaveRequest lectureSaveRequest, MultipartFile multipartFile) throws IOException {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        String fileUrl;
        if (multipartFile.isEmpty() || multipartFile == null){
            throw new BadRequestException(FILE_NOT_FOUND);
        }
        else {
            fileUrl = s3Service.uploadFile(multipartFile);
        }

        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int semester;
        if (month <= 6)
            semester = 1;
        else
            semester = 2;

        Lecture lecture = LectureSaveRequest.lectureToEntity(userEntity, fileUrl, year, semester, lectureSaveRequest);
        lectureRepository.save(lecture);

        File file = saveFileInLocal(multipartFile);
        requestFileToText("eng", lecture.getLectureId(), file);
    }

    private File saveFileInLocal(MultipartFile multipartFile) throws IOException {
        // 원본 파일명
        String originalFileName = multipartFile.getOriginalFilename();

        // 로컬 저장 경로에 저장할 파일 객체 생성
        File localFile = new File(LOCAL_STORAGE_PATH + originalFileName);
        localFile.getParentFile().mkdirs();

        // MultipartFile의 InputStream을 사용하여 파일을 로컬에 저장
        try (OutputStream outputStream = new FileOutputStream(localFile)) {
            byte[] bytes = multipartFile.getBytes();
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new IOException("Failed to store file " + originalFileName, e);
        }

        return localFile;
    }

    @Async
    @Transactional
    protected void requestFileToText(String category, Long lectureId, File file) throws IOException {
        // 대상 서버 URL
        String targetUrl = "http://59.29.138.9:5603/glee/asr";
        RestTemplate restTemplate = new RestTemplate();

        log.info("file: {}", file.getName());

        byte[] fileBytes = null;
        try {
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            // 예외 처리
            log.info("cant get bytes from file");
        }

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 요청 파라미터 및 파일 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("category", category);
        body.add("lectureId", lectureId);
        body.add("file", new HttpEntity<>(fileBytes, createFileHeaders(file.getName())));

        // HTTP 엔터티 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 서버로 POST 요청 전송
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        // 응답 확인
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String responseContents = jsonNode.get("contents").asText();
            log.info("Translate SUCCESS: {}", responseBody);

            Lecture lecture = lectureRepository.findById(lectureId)
                    .orElseThrow(() -> new BadRequestException(LECTURE_NOT_FOUND));

            Content content = Content.builder().content(responseContents).build();
            contentRepository.save(content);
            lecture.setContent(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Transactional
    public void requestTranslate(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new BadRequestException(LECTURE_NOT_FOUND));
        Content content = lecture.getContent();

        // 대상 서버 URL
        String targetUrl = "http://59.29.138.9:5603/glee/translate";
        RestTemplate restTemplate = new RestTemplate();

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 파라미터 및 파일 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("contents", content.getContent());
        body.add("language", "eng");

        // HTTP 엔터티 생성
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // 서버로 POST 요청 전송
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                targetUrl,
                HttpMethod.POST,
                requestEntity,
                String.class);

        // 응답 확인
        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        if (responseBody != null) {
            content.setTranslatedContent(responseBody);
        }
    }

    private static HttpHeaders createFileHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("file", fileName);
        return headers;
    }
}

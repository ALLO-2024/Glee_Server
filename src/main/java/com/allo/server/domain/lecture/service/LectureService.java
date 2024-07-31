package com.allo.server.domain.lecture.service;

import com.allo.server.domain.content.entity.Content;
import com.allo.server.domain.content.repository.ContentRepository;
import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponseByPartialTitle;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponseByYearAndSemester;
import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.domain.lecture.repository.CustomLectureRepository;
import com.allo.server.domain.lecture.repository.LectureRepository;
import com.allo.server.domain.user.entity.Language;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.global.s3.S3Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private final CustomLectureRepository customLectureRepository;
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
            CompletableFuture<URL> future = s3Service.uploadFile(multipartFile);
            fileUrl = future.thenApply(URL::toString).join();
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

        String language = userEntity.getLanguage().toString();
        File file = saveFileInLocal(multipartFile);
        Content content = new Content(lecture);
        contentRepository.save(content);
        lecture.setContent(content);

        saveContent("eng", lecture.getLectureId(), file, content, language);
    }

    private File saveFileInLocal(MultipartFile multipartFile) throws IOException {
        // 원본 파일명
        String originalFileName = multipartFile.getOriginalFilename();
        UUID fileNameUUID = UUID.randomUUID();
        String extenstion = Objects.requireNonNull(originalFileName).split("\\.")[1];


        // 로컬 저장 경로에 저장할 파일 객체 생성
        File localFile = new File(LOCAL_STORAGE_PATH + fileNameUUID + "." + extenstion);
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

    @Transactional
    public void saveContent(String category, Long lectureId, File file, Content content, String language) throws IOException {
        // 대상 서버 URL
        String targetUrl = "http://59.29.138.9:5603/glee/asr";
        RestTemplate restTemplate = new RestTemplate();

        log.info("file: {}", file.getName());

        byte[] fileBytes = null;
        try {
            log.info("read bytes from file");
            fileBytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            // 예외 처리
            log.info("cant get bytes from file");
        }

        log.info("read bytes from files success");

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

        log.info("post request to ai server");

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
            log.info("FILE TO TEXT SUCCESS: {}", responseBody);
            content.setContent(responseContents);

            // 한국어 요약
            requestSummary(content, responseContents, false);
            // 키워드
            requestKeywords(content);
            // 번역, 번역 요약
            requestContentInfo(content, language);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void requestContentInfo(Content content, String language) {
        String url = "http://59.29.138.9:5603/glee/translate";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));


        String requestBody = "{\"contents\":\"" + content.getContent() + "\",\"language\":\"" + language + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        log.info("content info request");

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        try {
            Pattern pattern = Pattern.compile("\\[(.*?)\\]");
            Matcher matcher = pattern.matcher(responseBody);

            if (matcher.find()) {
                String extractedText = matcher.group(1);
                String translated = extractedText.substring(2, extractedText.length() - 2);
                log.info("TRANSLATE SUCCESS: {}", translated);
                content.setTranslatedContent(translated);

                // 요약 요청
                requestSummary(content, translated, true);
            } else {
                log.info("Translated array is empty or not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void requestSummary(Content content, String reqString, boolean isTranslated) {
        String url = "http://59.29.138.9:5603/glee/summary";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String requestBody = "{\"contents\":\"" + reqString + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        log.info("summary request");
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        try {
            String extractedText = responseBody.substring(17, responseBody.length() - 4);
            if (!extractedText.isEmpty()) {
                if (isTranslated) {
                    log.info("TRANSLATED Summary SUCCESS: {}", responseBody);
                    log.info("Extracted text: {}", extractedText);
                    content.setTranslatedSummary(extractedText);
                } else {
                    log.info("KOREAN Summary SUCCESS: {}", responseBody);
                    log.info("Extracted text: {}", extractedText);
                    content.setSummary(extractedText);
                }
            } else {
                log.info("Summary field is not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void requestKeywords(Content content) {
        String url = "http://59.29.138.9:5603/glee/keyword";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String requestBody = "{\"contents\":\"" + content.getContent() + "\"}";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate = new RestTemplate();

        log.info("keyword request");

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        HttpStatusCode statusCode = responseEntity.getStatusCode();
        String responseBody = responseEntity.getBody();

        try {
            String extractedText = responseBody.substring(17, responseBody.length() - 4);
            if (!extractedText.isEmpty()) {
                log.info("KEYWORD SUCCESS: {}", responseBody);
                log.info("Extracted text: {}", extractedText);
                content.setKeywords(extractedText);
            } else {
                log.info("Summary field is not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static HttpHeaders createFileHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("file", fileName);
        return headers;
    }

    @Transactional
    public LectureSearchResponse getLecture(String email, Long lectureId) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        Lecture lecture = lectureRepository.getLectureByUserEntityAndLectureId(userEntity, lectureId);

        return new LectureSearchResponse(lecture.getLectureId(), lecture.getTitle(), lecture.getLectureType(), userEntity.getLanguage(), lecture.getContent().getContent(), lecture.getContent().getTranslatedContent(), lecture.getContent().getSummary(), lecture.getContent().getTranslatedSummary(), lecture.getContent().getKeywords());
    }

    @Transactional
    public List<LectureSearchResponseByYearAndSemester> getLectureByYearAndSemester(String email, int year, int semester) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        return customLectureRepository.getLectureByYearAndSemester(userEntity.getUserId(), year, semester);
    }

    @Transactional
    public List<LectureSearchResponseByPartialTitle> findLecturesByTitleContaining(String email, String title) {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        return customLectureRepository.findLecturesByTitleContaining(userEntity.getUserId(), title);
    }
}

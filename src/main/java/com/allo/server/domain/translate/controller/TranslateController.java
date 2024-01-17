package com.allo.server.domain.translate.controller;

import com.allo.server.domain.translate.data.VoiceToTextReqDto;
import com.allo.server.global.config.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/translate")
@Slf4j
public class TranslateController {

    @PostMapping("/test-request")
    public BaseResponse<String> sendMultipartTest(
            @RequestPart VoiceToTextReqDto reqDto,
            @RequestPart("file") MultipartFile multipartFile) {
        // 대상 서버 URL
        String targetUrl = "http://localhost:8080/translate/test-receive";
        RestTemplate restTemplate = new RestTemplate();

        byte[] fileBytes;
        try {
            fileBytes = multipartFile.getBytes();
        } catch (IOException e) {
            // 예외 처리
            log.info("cant get bytes from file");
            return new BaseResponse<>("fail");
        }

        // 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // 요청 파라미터 및 파일 설정
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("reqDto", reqDto);
        body.add("file", new HttpEntity<>(fileBytes, createFileHeaders(multipartFile.getOriginalFilename())));

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

        return new BaseResponse<>(responseBody);
    }

    @PostMapping("/test-receive")
    public BaseResponse<String> receiveRequest(
            @RequestPart("reqDto") VoiceToTextReqDto reqDto,
            @RequestPart("file") MultipartFile multipartFile) {
        return new BaseResponse<>("success");
    }

    private static HttpHeaders createFileHeaders(String fileName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("file", fileName);
        return headers;
    }

}

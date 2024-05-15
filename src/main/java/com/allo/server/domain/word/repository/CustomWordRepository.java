package com.allo.server.domain.word.repository;

import com.allo.server.domain.word.dto.response.WordGetResponse;

import org.springframework.data.domain.Pageable;
import java.util.List;


public interface CustomWordRepository {

    // 강의 조회
    List<WordGetResponse> getWords(Long userId, Pageable pageable);

}
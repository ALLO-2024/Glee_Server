package com.allo.server.domain.lecture.repository;

import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;

import java.util.List;

public interface CustomLectureRepository {

    // 강의 조회
    List<LectureSearchResponse> getLectures(Long userId, int year, int semester);

}
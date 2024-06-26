package com.allo.server.domain.lecture.repository;

import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponseByPartialTitle;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponseByYearAndSemester;
import com.allo.server.domain.lecture.entity.Lecture;

import java.util.List;

public interface CustomLectureRepository {

    // 년도별 학기별 강의 조회
    List<LectureSearchResponseByYearAndSemester> getLectureByYearAndSemester(Long userId, int year, int semester);

    // 제목 일부로 강의 조회
    List<LectureSearchResponseByPartialTitle> findLecturesByTitleContaining(Long userId, String title);
}
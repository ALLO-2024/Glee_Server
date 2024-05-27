package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.entity.LectureType;
import java.sql.Timestamp;
import java.util.List;

public record LectureSearchResponseByYearAndSemester (Long lectureId, String title,
                                     LectureType lectureType,
                                     List<String> keywords,
                                     Timestamp createdAt) {
}
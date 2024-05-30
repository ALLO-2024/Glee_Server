package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.entity.LectureType;

public record LectureSearchResponseByYearAndSemester (Long lectureId, String title,
                                                      LectureType lectureType,
                                                      String keywords,
                                                      String createdAt) {
}

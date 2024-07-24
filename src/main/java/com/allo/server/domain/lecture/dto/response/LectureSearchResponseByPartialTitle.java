package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.entity.LectureType;

public record LectureSearchResponseByPartialTitle (Long lectureId,
                                                      String title,
                                                      LectureType lectureType,
                                                      String keywords,
                                                      String createdAt) {
}
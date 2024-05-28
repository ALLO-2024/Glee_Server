package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.entity.LectureType;

import java.util.List;

public record LectureSearchResponse (Long lectureId, String title,
                                    LectureType lectureType,
                                    String Content,
                                    String translatedContent,
                                    String summary,
                                     String keywords) {
}

package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.entity.LectureType;
import com.allo.server.domain.user.entity.Language;


public record LectureSearchResponse (Long lectureId,
                                     String title,
                                     LectureType lectureType,
                                     Language language,
                                     String Content,
                                     String translatedContent,
                                     String summary,
                                     String translatedSummary,
                                     String keywords) {
}

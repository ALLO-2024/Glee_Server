package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.entity.LectureType;

import java.sql.Timestamp;

public record LectureSearchResponse (Long lectureId, String title,
                                    LectureType lectureType,
                                    Timestamp createdAt)
{
}

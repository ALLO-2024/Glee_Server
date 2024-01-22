package com.allo.server.domain.lecture.entity;

import com.allo.server.error.exception.custom.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.allo.server.error.ErrorCode.UNKNOWN_LECTURE_SUBJECT;

public enum LectureSubject {
    HUMANITY, SOCIAL, EDUCATION, SCIENCE, ENTERTAINMENT, CULTURE; // HUMANITY : 인문, SOCIAL : 사회, EDUCATION : 교육, SCIENCE : 공학, ENTERTAINMENT: 예체능, CULTURE : 교양

    @JsonCreator // 이 어노테이션은 Jackson이 JSON에서 객체로 변환할 때 사용
    public static LectureSubject fromString(String value) {
        for (LectureSubject type : LectureSubject.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BadRequestException(UNKNOWN_LECTURE_SUBJECT);
    }
}

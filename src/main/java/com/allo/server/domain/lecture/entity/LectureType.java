package com.allo.server.domain.lecture.entity;

import com.allo.server.error.exception.custom.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.allo.server.error.ErrorCode.UNKNOWN_LECTURE_TYPE;

public enum LectureType {
    MAJOR, CULTURE_ESSENTIAL, CULTURE_SELECT; // MAJOR : 전공 , CULTURE_ESSENTIAL : 교양 필수, CULTURE_SELECT : 교양 선택

    @JsonCreator // 이 어노테이션은 Jackson이 JSON에서 객체로 변환할 때 사용
    public static LectureType fromString(String value) {
        for (LectureType type : LectureType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BadRequestException(UNKNOWN_LECTURE_TYPE);
    }
}

package com.allo.server.domain.lecture.entity;

import com.allo.server.error.exception.custom.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;

import static com.allo.server.error.ErrorCode.UNKNOWN_LECTURE_LANGUAGE;

public enum LectureLanguage {
    ENGLISH, CHINESE, JAPANESE, VIETNAMESE; // ENGLISH : 영어, CHINESE : 중국어, JAPANESE : 일본어, VIETNAMESE : 베트남어

    @JsonCreator // 이 어노테이션은 Jackson이 JSON에서 객체로 변환할 때 사용
    public static LectureLanguage fromString(String value) {
        for (LectureLanguage type : LectureLanguage.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BadRequestException(UNKNOWN_LECTURE_LANGUAGE);
    }
}
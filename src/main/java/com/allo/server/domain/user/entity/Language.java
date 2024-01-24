package com.allo.server.domain.user.entity;

import com.allo.server.error.exception.custom.BadRequestException;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import static com.allo.server.error.ErrorCode.UNKNOWN_LECTURE_LANGUAGE;

@Getter
@RequiredArgsConstructor
public enum Language {
    ENGLISH("1"),
    CHINESE("11"),
    JAPANESE("2"),
    VIETNAMESE("7"); // ENGLISH : 영어, CHINESE : 중국어, JAPANESE : 일본어, VIETNAMESE : 베트남어

    private final String key;

    @JsonCreator // 이 어노테이션은 Jackson이 JSON에서 객체로 변환할 때 사용
    public static Language fromString(String value) {
        for (Language type : Language.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new BadRequestException(UNKNOWN_LECTURE_LANGUAGE);
    }
}
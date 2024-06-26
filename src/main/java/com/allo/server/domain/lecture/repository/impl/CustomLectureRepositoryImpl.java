package com.allo.server.domain.lecture.repository.impl;

import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.domain.lecture.entity.QLecture;
import com.allo.server.domain.lecture.repository.CustomLectureRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.allo.server.domain.lecture.entity.QLecture.lecture;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomLectureRepositoryImpl implements CustomLectureRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<LectureSearchResponse> getLectures(Long userId, int year, int semester) {
        return queryFactory
                .select(Projections.constructor(LectureSearchResponse.class, lecture.lectureId, lecture.title, lecture.lectureType, lecture.createdAt))
                .from(lecture)
                .where(lecture.userEntity.userId.eq(userId)
                        .and(lecture.year.eq(year))
                        .and(lecture.semester.eq(semester)))
                .fetch();
    }
}
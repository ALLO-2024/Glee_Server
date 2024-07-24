package com.allo.server.domain.lecture.repository.impl;

import com.allo.server.domain.lecture.dto.response.LectureSearchResponseByPartialTitle;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponseByYearAndSemester;
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
    public List<LectureSearchResponseByYearAndSemester> getLectureByYearAndSemester(Long userId, int year, int semester) {
        return queryFactory
                .select(Projections.constructor(LectureSearchResponseByYearAndSemester.class,
                        lecture.lectureId,
                        lecture.title,
                        lecture.lectureType,
                        lecture.content.keywords,
                        lecture.createdAt.stringValue().substring(0, 16))) // Timestamp를 문자열로 변환하여 yyyy-MM-dd HH:mm 형식으로 잘라서 사용
                .from(lecture)
                .where(lecture.userEntity.userId.eq(userId)
                        .and(lecture.year.eq(year))
                        .and(lecture.semester.eq(semester)))
                .fetch();
    }

    @Override
    public List<LectureSearchResponseByPartialTitle> findLecturesByTitleContaining(Long userId, String title) {
        return queryFactory
                .select(Projections.constructor(LectureSearchResponseByPartialTitle.class,
                        lecture.lectureId,
                        lecture.title,
                        lecture.lectureType,
                        lecture.content.keywords,
                        lecture.createdAt.stringValue().substring(0, 16))) // Timestamp를 문자열로 변환하여 yyyy-MM-dd HH:mm 형식으로 잘라서 사용
                .from(lecture)
                .where(lecture.userEntity.userId.eq(userId)
                        .and(lecture.title.contains(title)))
                .fetch();
    }
}
package com.allo.server.domain.lecture.repository;

import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

    List<Lecture> findByTitleContaining(String searchContent);
    List<Lecture> findAllByUserEntityAndYearAndSemester(UserEntity userEntity, int year, int semester);
    Lecture getLectureByUserEntityAndLectureId(UserEntity userEntity, Long lectureId);

}

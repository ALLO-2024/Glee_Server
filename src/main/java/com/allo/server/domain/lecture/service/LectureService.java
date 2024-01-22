package com.allo.server.domain.lecture.service;

import com.allo.server.domain.lecture.dto.request.LectureSaveRequest;
import com.allo.server.domain.lecture.dto.response.LectureSearchResponse;
import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.domain.lecture.repository.LectureRepository;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.domain.user.repository.UserRepository;
import com.allo.server.error.exception.custom.BadRequestException;
import com.allo.server.global.s3.S3Service;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.allo.server.error.ErrorCode.FILE_NOT_FOUND;
import static com.allo.server.error.ErrorCode.USER_NOT_FOUND;

@Service
@Transactional
@AllArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service;

    public void saveLecture(String email, LectureSaveRequest lectureSaveRequest, MultipartFile multipartFile) throws IOException {

        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() -> new BadRequestException(USER_NOT_FOUND));

        String fileUrl;
        if (multipartFile.isEmpty() || multipartFile == null){
            throw new BadRequestException(FILE_NOT_FOUND);
        }
        else {
            fileUrl = s3Service.uploadFile(multipartFile);
        }

        LocalDate localDate = LocalDate.now();
        int year = localDate.getYear();
        int month = localDate.getMonthValue();
        int semester;
        if (month <= 6)
            semester = 1;
        else
            semester = 2;

        Lecture lecture = LectureSaveRequest.lectureToEntity(userEntity, fileUrl, year, semester, lectureSaveRequest);
        lectureRepository.save(lecture);

    }

}

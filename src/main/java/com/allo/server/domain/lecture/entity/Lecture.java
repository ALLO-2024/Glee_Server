package com.allo.server.domain.lecture.entity;

import com.allo.server.domain.user.entity.Role;
import com.allo.server.domain.user.entity.SocialType;
import com.allo.server.domain.user.entity.UserEntity;
import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lectureId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    private String fileUrl;

    @Column(length = 25)
    private String title;

    private int year;

    private int semester;

    @Enumerated(EnumType.STRING)
    private LectureLanguage lectureLanguage;

    @Enumerated(EnumType.STRING)
    private LectureType lectureType;

    @Enumerated(EnumType.STRING)
    private LectureSubject lectureSubject;

    @Builder
    public Lecture(UserEntity userEntity, String fileUrl, String title, int year, int semester, LectureLanguage lectureLanguage, LectureType lectureType, LectureSubject lectureSubject) {
        this.userEntity = userEntity;
        this.fileUrl = fileUrl;
        this.title = title;
        this.year = year;
        this.semester = semester;
        this.lectureLanguage = lectureLanguage;
        this.lectureType = lectureType;
        this.lectureSubject = lectureSubject;
    }
}
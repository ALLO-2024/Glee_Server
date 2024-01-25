package com.allo.server.domain.content.entity;

import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Content extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contentId;

    @OneToOne
    private Lecture lecture;

    @Lob
    private String content;

    @Lob
    private String translatedContent;

    public void setTranslatedContent(String translatedContent) {
        this.translatedContent = translatedContent;
    }
}

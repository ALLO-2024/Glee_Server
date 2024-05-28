package com.allo.server.domain.content.entity;

import com.allo.server.domain.lecture.entity.Lecture;
import com.allo.server.global.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
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

    @Lob
    private String summary;

    @Lob
    private String translatedSummary;

    private String keywords;

    public void setContent(String content) {
        this.content = content;
    }

    public void setTranslatedContent(String translatedContent) {
        this.translatedContent = translatedContent;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setTranslatedSummary(String translatedSummary) {
        this.translatedSummary = translatedSummary;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}

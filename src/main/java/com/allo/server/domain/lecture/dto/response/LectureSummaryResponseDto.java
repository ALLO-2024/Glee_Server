package com.allo.server.domain.lecture.dto.response;

import com.allo.server.domain.lecture.dto.Choice;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class LectureSummaryResponseDto {
    private String id;
    private String object;
    private LocalDate created;
    private String model;
    private List<Choice> choices;
}

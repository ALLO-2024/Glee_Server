package com.allo.server.domain.lecture.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Choice {
    private String text;
    private Integer index;
    @JsonProperty("finish_reason")
    private String finishReason;
}

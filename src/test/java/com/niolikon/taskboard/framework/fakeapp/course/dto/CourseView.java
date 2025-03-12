package com.niolikon.taskboard.framework.fakeapp.course.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CourseView {
    @JsonProperty("Id")
    private Long id;

    @JsonProperty("Title")
    private String title;
}

package com.niolikon.taskboard.framework.exceptions.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemDetails {
    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
}


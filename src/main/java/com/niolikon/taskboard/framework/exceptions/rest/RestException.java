package com.niolikon.taskboard.framework.exceptions.rest;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private final HttpStatus status;
    private final String type;
    private final String title;
    private final String detail;

    public RestException(HttpStatus status, String type, String title, String detail) {
        super(detail);
        this.status = status;
        this.type = type;
        this.title = title;
        this.detail = detail;
    }

    public RestException(HttpStatus status, String title, String detail) {
        super(detail);
        this.status = status;
        this.type = "https://httpstatuses.io/" + status.value();
        this.title = title;
        this.detail = detail;
    }

    public RestException(HttpStatus status, String detail) {
        super(detail);
        this.status = status;
        this.type = "https://httpstatuses.io/" + status.value();
        this.title = status.name();
        this.detail = detail;
    }
}

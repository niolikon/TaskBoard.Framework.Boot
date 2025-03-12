package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class BadRequestRestException extends RestException {
    public BadRequestRestException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

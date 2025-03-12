package com.niolikon.taskboard.framework.exceptions.rest.server;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class InternalServerErrorRestException extends RestException {
    public InternalServerErrorRestException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}

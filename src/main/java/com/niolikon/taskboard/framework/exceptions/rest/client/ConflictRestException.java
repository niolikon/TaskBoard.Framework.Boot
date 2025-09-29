package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class ConflictRestException extends RestException {
    public ConflictRestException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}


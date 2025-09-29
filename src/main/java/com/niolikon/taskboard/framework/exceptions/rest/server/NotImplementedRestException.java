package com.niolikon.taskboard.framework.exceptions.rest.server;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class NotImplementedRestException extends RestException {
    public NotImplementedRestException(String message) {
        super(HttpStatus.NOT_IMPLEMENTED, message);
    }
}

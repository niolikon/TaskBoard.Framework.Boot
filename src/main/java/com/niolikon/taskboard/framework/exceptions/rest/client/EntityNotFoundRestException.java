package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundRestException extends RestException {
    public EntityNotFoundRestException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}

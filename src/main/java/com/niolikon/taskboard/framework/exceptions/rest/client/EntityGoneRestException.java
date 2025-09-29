package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class EntityGoneRestException extends RestException {
    public EntityGoneRestException(String message) {
        super(HttpStatus.GONE, message);
    }
}

package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class LengthRequiredRestException extends RestException {
    public LengthRequiredRestException(String message) {
        super(HttpStatus.LENGTH_REQUIRED, message);
    }
}

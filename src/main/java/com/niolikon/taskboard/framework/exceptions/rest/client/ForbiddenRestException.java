package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class ForbiddenRestException  extends RestException {
    public ForbiddenRestException(String message) {
        super(HttpStatus.FORBIDDEN, message);
    }
}

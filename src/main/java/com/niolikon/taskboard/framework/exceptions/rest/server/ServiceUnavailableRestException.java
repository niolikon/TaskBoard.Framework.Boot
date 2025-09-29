package com.niolikon.taskboard.framework.exceptions.rest.server;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class ServiceUnavailableRestException extends RestException {
    public ServiceUnavailableRestException(String message) {
        super(HttpStatus.SERVICE_UNAVAILABLE, message);
    }
}

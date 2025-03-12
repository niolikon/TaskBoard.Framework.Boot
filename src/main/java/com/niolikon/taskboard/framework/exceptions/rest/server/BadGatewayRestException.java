package com.niolikon.taskboard.framework.exceptions.rest.server;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class BadGatewayRestException extends RestException {
    public BadGatewayRestException(String message) {
        super(HttpStatus.BAD_GATEWAY, message);
    }
}

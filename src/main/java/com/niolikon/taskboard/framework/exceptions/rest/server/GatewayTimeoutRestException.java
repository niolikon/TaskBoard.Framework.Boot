package com.niolikon.taskboard.framework.exceptions.rest.server;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class GatewayTimeoutRestException extends RestException {
    public GatewayTimeoutRestException(String message) {
        super(HttpStatus.GATEWAY_TIMEOUT, message);
    }
}

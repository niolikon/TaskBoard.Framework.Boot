package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class PayloadTooLargeRestException extends RestException {
    public PayloadTooLargeRestException(String message) {
        super(HttpStatus.PAYLOAD_TOO_LARGE, message);
    }
}

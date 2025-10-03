package com.niolikon.taskboard.framework.exceptions.rest.client;

import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import org.springframework.http.HttpStatus;

public class FailedDependencyRestException extends RestException {
    public FailedDependencyRestException(String message) {
        super(HttpStatus.FAILED_DEPENDENCY, message);
    }
}

package com.niolikon.taskboard.framework.exceptions;

import com.niolikon.taskboard.framework.exceptions.dto.ProblemDetails;
import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ConditionalOnProperty(name = "taskboard.exceptions.handler.enabled", havingValue = "true")
public class GlobalExceptionHandler {

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ProblemDetails> handleRestException(RestException ex, HttpServletRequest request) {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .type(ex.getType())
                .title(ex.getTitle())
                .status(ex.getStatus().value())
                .detail(ex.getDetail())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(problemDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGenericException(Exception ex, HttpServletRequest request) {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .type("https://httpstatuses.io/500")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetails);
    }
}

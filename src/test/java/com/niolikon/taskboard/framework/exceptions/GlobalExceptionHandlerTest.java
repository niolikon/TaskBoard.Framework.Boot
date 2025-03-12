package com.niolikon.taskboard.framework.exceptions;

import com.niolikon.taskboard.framework.exceptions.dto.ProblemDetails;
import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);
    }

    @Test
    void givenRestException_whenHandleRestException_thenReturnsCorrectResponse() {
        RestException restException = new RestException(
                HttpStatus.BAD_REQUEST,
                "https://httpstatuses.io/400",
                "Bad request",
                "Something went wrong"
        );
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ProblemDetails> response = exceptionHandler.handleRestException(restException, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .extracting(
                        ProblemDetails::getType,
                        ProblemDetails::getTitle,
                        ProblemDetails::getStatus,
                        ProblemDetails::getDetail,
                        ProblemDetails::getInstance)
                .containsExactly(
                        "https://httpstatuses.io/400",
                        "Bad request",
                        HttpStatus.BAD_REQUEST.value(),
                        "Something went wrong",
                        "/api/test"
                        );
    }

    @Test
    void givenGenericException_whenHandleGenericException_thenReturnsInternalServerErrorResponse() {
        Exception genericException = new Exception("Unexpected error occurred");
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ProblemDetails> response = exceptionHandler.handleGenericException(genericException, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertThat(response.getBody())
                .isNotNull()
                .extracting(
                        ProblemDetails::getType,
                        ProblemDetails::getTitle,
                        ProblemDetails::getStatus,
                        ProblemDetails::getDetail,
                        ProblemDetails::getInstance)
                .containsExactly(
                        "https://httpstatuses.io/500",
                        "Internal Server Error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Unexpected error occurred",
                        "/api/test"
                );
    }
}

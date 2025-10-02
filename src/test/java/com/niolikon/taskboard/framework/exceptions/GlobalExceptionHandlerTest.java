package com.niolikon.taskboard.framework.exceptions;

import com.niolikon.taskboard.framework.exceptions.dto.ProblemDetails;
import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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


    static Stream<Arguments> provideSpringExceptionForBadRequest() {
        BindingResult bindingResultForManve = mock(BindingResult.class);
        when(bindingResultForManve.getFieldErrors()).thenReturn(
                List.of(new FieldError("userDto", "name", "must not be blank"))
        );
        when(bindingResultForManve.getGlobalErrors()).thenReturn(List.of());
        MethodArgumentNotValidException methodArgumentNotValid = mock(MethodArgumentNotValidException.class);
        when(methodArgumentNotValid.getBindingResult()).thenReturn(bindingResultForManve);
        String expectedManve = "name: must not be blank";

        HandlerMethodValidationException handlerMethodValidation = mock(HandlerMethodValidationException.class);
        when(handlerMethodValidation.getMessage()).thenReturn("Method validation failed");
        String expectedHmve = "Method validation failed";

        MissingServletRequestPartException missingPart = new MissingServletRequestPartException("file");
        String expectedMissingPart = "Missing part: file";

        MissingServletRequestParameterException missingParam =
                new MissingServletRequestParameterException("q", "String");
        String expectedMissingParam = "Missing parameter: q (type String)";

        MethodArgumentTypeMismatchException mismatch =
                new MethodArgumentTypeMismatchException("abc", Integer.class, "id", null, new IllegalArgumentException("x"));
        String expectedMismatch = "Parameter 'id' with value 'abc' is not of required type Integer";

        RuntimeException mostSpecific = new RuntimeException("Unexpected character ('}' at position 10)");
        HttpMessageNotReadableException notReadable = new HttpMessageNotReadableException("wrapper msg", mostSpecific);
        String expectedNotReadable = "Malformed request body: Unexpected character ('}' at position 10)";

        MultipartException multipartWithMsg = new MultipartException("Size limit exceeded");
        String expectedMultipartWithMsg = "Multipart error: Size limit exceeded";
        MultipartException multipartNullMsg = new MultipartException(null);
        String expectedMultipartNullMsg = "Multipart error: Invalid multipart request";

        Object target = new Object();
        DataBinder binder = new DataBinder(target, "userDto");
        BindException bindException = new BindException(binder.getBindingResult());
        bindException.addError(new FieldError("userDto", "email", "must be a well-formed email address"));
        String expectedBind = "email: must be a well-formed email address";

        @SuppressWarnings("unchecked")
        ConstraintViolation<Object> v1 = mock(ConstraintViolation.class);
        Path path1 = mock(Path.class);
        when(path1.toString()).thenReturn("user.name");
        when(v1.getPropertyPath()).thenReturn(path1);
        when(v1.getMessage()).thenReturn("must not be blank");

        ConstraintViolation<Object> v2 = mock(ConstraintViolation.class);
        Path path2 = mock(Path.class);
        when(path2.toString()).thenReturn("age");
        when(v2.getPropertyPath()).thenReturn(path2);
        when(v2.getMessage()).thenReturn("must be >= 18");

        ConstraintViolationException constraintViolation =
                new ConstraintViolationException(Set.of(v1, v2));
        String expectedConstraint = "age: must be >= 18; user.name: must not be blank";

        return Stream.of(
                Arguments.of(methodArgumentNotValid, expectedManve),
                Arguments.of(handlerMethodValidation, expectedHmve),
                Arguments.of(missingPart, expectedMissingPart),
                Arguments.of(missingParam, expectedMissingParam),
                Arguments.of(mismatch, expectedMismatch),
                Arguments.of(notReadable, expectedNotReadable),
                Arguments.of(multipartWithMsg, expectedMultipartWithMsg),
                Arguments.of(multipartNullMsg, expectedMultipartNullMsg),
                Arguments.of(bindException, expectedBind),
                Arguments.of(constraintViolation, expectedConstraint)
        );
    }

    @ParameterizedTest
    @MethodSource("provideSpringExceptionForBadRequest")
    void givenSpringException_whenExceptionRelatedToABadRequest_thenReturnsBadRequestErrorResponse(Exception e, String expectedDetail) {
        when(request.getRequestURI()).thenReturn("/api/test");

        ResponseEntity<ProblemDetails> response = exceptionHandler.handleGenericException(e, request);

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
                        "Bad Request",
                        HttpStatus.BAD_REQUEST.value(),
                        expectedDetail,
                        "/api/test"
                );
    }
}

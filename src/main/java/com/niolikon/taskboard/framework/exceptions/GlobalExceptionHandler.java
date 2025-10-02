package com.niolikon.taskboard.framework.exceptions;

import com.niolikon.taskboard.framework.exceptions.dto.ProblemDetails;
import com.niolikon.taskboard.framework.exceptions.rest.RestException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestControllerAdvice
@ConditionalOnProperty(name = "taskboard.exceptions.handler.enabled", havingValue = "true")
public class GlobalExceptionHandler {

    public ResponseEntity<ProblemDetails> mapRestExceptionToProblemDetails(RestException ex, HttpServletRequest request)
    {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .type(ex.getType())
                .title(ex.getTitle())
                .status(ex.getStatus().value())
                .detail(ex.getDetail())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(problemDetails);
    }

    @ExceptionHandler(RestException.class)
    public ResponseEntity<ProblemDetails> handleRestException(RestException ex, HttpServletRequest request) {
        return mapRestExceptionToProblemDetails(ex, request);
    }

    public ResponseEntity<ProblemDetails> mapExceptionToBadRequest(String message, HttpServletRequest request)
    {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .type("https://httpstatuses.io/400")
                .title("Bad Request")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail(message)
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetails);
    }

    public ResponseEntity<ProblemDetails> mapExceptionToInternalServerError(Exception ex, HttpServletRequest request)
    {
        ProblemDetails problemDetails = ProblemDetails.builder()
                .type("https://httpstatuses.io/500")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetails);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGenericException(Exception ex, HttpServletRequest request) {
        if (ex instanceof MethodArgumentNotValidException manve) {
            String detail = extractFromBindingResult(manve);
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof HandlerMethodValidationException hmve) {
            String detail = hmve.getMessage();
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof MissingServletRequestPartException msrpe) {
            String detail = "Missing part: " + nullSafe(msrpe.getRequestPartName(), "<unknown>");
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof MissingServletRequestParameterException msrpe) {
            String detail = "Missing parameter: " + msrpe.getParameterName() +
                    (msrpe.getParameterType() != null ? " (type " + msrpe.getParameterType() + ")" : "");
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof MethodArgumentTypeMismatchException matme) {
            String required = matme.getRequiredType() != null ? matme.getRequiredType().getSimpleName() : "?";
            String detail = "Parameter '" + matme.getName() + "' with value '" + matme.getValue() +
                    "' is not of required type " + required;
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof HttpMessageNotReadableException hmnre) {
            String msg = hmnre.getMostSpecificCause() != null && hmnre.getMostSpecificCause().getMessage() != null
                    ? hmnre.getMostSpecificCause().getMessage()
                    : hmnre.getMessage();
            String detail = "Malformed request body: " + msg;
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof MultipartException mpe) {
            String detail = "Multipart error: " + nonBlankOr(mpe.getMessage(), "Invalid multipart request");
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof BindException be) {
            String detail = extractFromBindingResult(be);
            return mapExceptionToBadRequest(detail, request);

        } else if (ex instanceof ConstraintViolationException cve) {
            String detail = cve.getConstraintViolations().stream()
                    .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                    .collect(java.util.stream.Collectors.joining("; "));
            return mapExceptionToBadRequest(detail, request);
        }

        return mapExceptionToInternalServerError(ex, request);
    }

    // Helpers

    private String extractFromBindingResult(org.springframework.validation.BindException ex) {
        var br = ex.getBindingResult();
        if (br == null || (br.getFieldErrors().isEmpty() && br.getGlobalErrors().isEmpty())) {
            return nonBlankOr(ex.getMessage(), "Validation failed");
        }
        String fieldErrors = br.getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + nonBlankOr(fe.getDefaultMessage(), "invalid"))
                .collect(java.util.stream.Collectors.joining("; "));
        String objectErrors = br.getGlobalErrors().stream()
                .map(ge -> ge.getObjectName() + ": " + nonBlankOr(ge.getDefaultMessage(), "invalid"))
                .collect(java.util.stream.Collectors.joining("; "));
        if (!objectErrors.isBlank()) {
            return fieldErrors.isBlank() ? objectErrors : fieldErrors + "; " + objectErrors;
        }
        return fieldErrors;
    }

    private String nullSafe(String s, String fallback) {
        return s != null ? s : fallback;
    }

    private String nonBlankOr(String s, String fallback) {
        return (s != null && !s.isBlank()) ? s : fallback;
    }
}

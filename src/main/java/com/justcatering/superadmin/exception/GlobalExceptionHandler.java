package com.justcatering.superadmin.exception;

import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.ErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that maps exceptions to the standard {@link ApiResponse} format.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles application-specific {@link BaseException} subtypes.
     *
     * @param ex      exception
     * @param request HTTP request
     * @return standardized error response
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse<Void>> handleBaseException(
            BaseException ex,
            HttpServletRequest request
    ) {
        log.warn("Business/application error on {}: {} [{}]",
                request.getRequestURI(), ex.getMessage(), ex.getErrorCode());
        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.failure(ex.getMessage(), ex.getStatus().value()));
    }

    /**
     * Handles Bean Validation failures on request bodies.
     *
     * @param ex validation exception
     * @return standardized validation error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<ErrorDetail> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toErrorDetail)
                .collect(Collectors.toList());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure("Validation failed", errors, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Handles malformed JSON or invalid enum values in request bodies.
     *
     * @param ex unreadable message exception
     * @return bad request response
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Void>> handleUnreadableMessage(HttpMessageNotReadableException ex) {
        String message = "Invalid request body";
        if (ex.getCause() != null && ex.getCause().getMessage() != null) {
            message = ex.getCause().getMessage();
        }
        log.warn("Malformed request body: {}", message);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.failure(message, HttpStatus.BAD_REQUEST.value()));
    }

    /**
     * Handles Spring Security authentication failures.
     *
     * @param ex authentication exception
     * @return unauthorized response
     */
    @ExceptionHandler({
            AuthenticationException.class,
            BadCredentialsException.class,
            DisabledException.class,
            LockedException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(Exception ex) {
        log.warn("Authentication failure: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.failure(ex.getMessage(), HttpStatus.UNAUTHORIZED.value()));
    }

    /**
     * Handles Spring Security access-denied failures.
     *
     * @param ex access denied exception
     * @return forbidden response
     */
    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<ApiResponse<Void>> handleAccessDenied(
            org.springframework.security.access.AccessDeniedException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.failure("Access denied", HttpStatus.FORBIDDEN.value()));
    }

    /**
     * Handles database access failures.
     *
     * @param ex data access exception
     * @return internal server error response
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataAccess(DataAccessException ex) {
        log.error("Database error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "A database error occurred",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    /**
     * Handles all uncaught exceptions.
     *
     * @param ex      unexpected exception
     * @param request HTTP request
     * @return internal server error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(
            Exception ex,
            HttpServletRequest request
    ) {
        log.error("Unexpected error on {}", request.getRequestURI(), ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.failure(
                        "An unexpected error occurred. Please try again later",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ));
    }

    private ErrorDetail toErrorDetail(FieldError fieldError) {
        return ErrorDetail.builder()
                .field(fieldError.getField())
                .message(fieldError.getDefaultMessage())
                .code("VALIDATION_ERROR")
                .build();
    }
}

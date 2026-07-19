package com.justcatering.superadmin.exception;

import java.io.Serial;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base runtime exception for all application business and technical errors.
 */
@Getter
public class BaseException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    /** HTTP status associated with this exception. */
    private final HttpStatus status;

    /** Optional machine-readable error code. */
    private final String errorCode;

    /**
     * Creates a base exception.
     *
     * @param message   error message
     * @param status    HTTP status
     * @param errorCode machine-readable code
     */
    public BaseException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    /**
     * Creates a base exception with a cause.
     *
     * @param message   error message
     * @param status    HTTP status
     * @param errorCode machine-readable code
     * @param cause     root cause
     */
    public BaseException(String message, HttpStatus status, String errorCode, Throwable cause) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }
}

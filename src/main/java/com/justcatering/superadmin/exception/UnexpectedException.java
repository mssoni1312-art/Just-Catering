package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown for unexpected system failures.
 */
public class UnexpectedException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an unexpected exception.
     *
     * @param message error message
     * @param cause   root cause
     */
    public UnexpectedException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "UNEXPECTED_ERROR", cause);
    }
}

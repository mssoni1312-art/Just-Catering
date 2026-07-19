package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when request validation fails beyond standard Bean Validation.
 */
public class ValidationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a validation exception.
     *
     * @param message error message
     */
    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "VALIDATION_ERROR");
    }
}

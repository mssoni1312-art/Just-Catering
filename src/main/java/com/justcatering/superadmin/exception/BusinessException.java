package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a business rule is violated.
 */
public class BusinessException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a business exception with HTTP 400.
     *
     * @param message error message
     */
    public BusinessException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "BUSINESS_ERROR");
    }

    /**
     * Creates a business exception with a custom error code.
     *
     * @param message   error message
     * @param errorCode machine-readable code
     */
    public BusinessException(String message, String errorCode) {
        super(message, HttpStatus.BAD_REQUEST, errorCode);
    }
}

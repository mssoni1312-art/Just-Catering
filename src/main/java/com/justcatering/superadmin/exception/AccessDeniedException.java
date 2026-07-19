package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when an authenticated user lacks required permissions.
 */
public class AccessDeniedException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an access-denied exception.
     *
     * @param message error message
     */
    public AccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN, "ACCESS_DENIED");
    }
}

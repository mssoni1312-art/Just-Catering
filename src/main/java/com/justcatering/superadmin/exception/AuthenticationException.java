package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when authentication fails.
 */
public class AuthenticationException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an authentication exception.
     *
     * @param message error message
     */
    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "AUTHENTICATION_ERROR");
    }
}

package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a database operation fails unexpectedly.
 */
public class DatabaseException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a database exception.
     *
     * @param message error message
     * @param cause   root cause
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE_ERROR", cause);
    }
}

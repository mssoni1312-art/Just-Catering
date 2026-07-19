package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested entity cannot be found.
 */
public class EntityNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates an entity-not-found exception.
     *
     * @param message error message
     */
    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "ENTITY_NOT_FOUND");
    }

    /**
     * Creates an entity-not-found exception for a typed resource.
     *
     * @param entityName entity type name
     * @param identifier identifier value
     */
    public EntityNotFoundException(String entityName, Object identifier) {
        super(
                entityName + " not found with identifier: " + identifier,
                HttpStatus.NOT_FOUND,
                "ENTITY_NOT_FOUND"
        );
    }
}

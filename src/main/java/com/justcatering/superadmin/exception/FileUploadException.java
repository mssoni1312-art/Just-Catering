package com.justcatering.superadmin.exception;

import java.io.Serial;
import org.springframework.http.HttpStatus;

/**
 * Thrown when a file upload fails validation or storage.
 */
public class FileUploadException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Creates a file-upload exception.
     *
     * @param message error message
     */
    public FileUploadException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "FILE_UPLOAD_ERROR");
    }
}

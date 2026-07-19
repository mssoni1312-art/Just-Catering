package com.justcatering.superadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response payload for a successful file upload.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {

    /** Stored file name (UUID-based). */
    private String fileName;

    /** Original uploaded file name. */
    private String originalName;

    /** Detected content type. */
    private String contentType;

    /** File size in bytes. */
    private long size;

    /** Public URL path to access the file. */
    private String url;
}

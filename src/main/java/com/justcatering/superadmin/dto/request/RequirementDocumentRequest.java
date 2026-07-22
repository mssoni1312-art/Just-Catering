package com.justcatering.superadmin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Uploaded document payload for requirement create.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementDocumentRequest {

    /** Original file name. */
    @NotBlank(message = "Document file name is required")
    @Size(max = 255, message = "Document file name must not exceed 255 characters")
    private String fileName;

    /** Stored file URL. */
    @NotBlank(message = "Document URL is required")
    @Size(max = 500, message = "Document URL must not exceed 500 characters")
    private String url;

    /** File size in bytes. */
    @Min(value = 0, message = "Document size must be zero or positive")
    private Long sizeBytes;

    /** MIME content type. */
    @Size(max = 100, message = "Document content type must not exceed 100 characters")
    private String contentType;
}

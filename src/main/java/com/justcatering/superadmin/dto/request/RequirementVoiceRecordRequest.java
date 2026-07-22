package com.justcatering.superadmin.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Uploaded voice recording payload for requirement create/update.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementVoiceRecordRequest {

    /** Stored voice file URL (from file upload API). */
    @Size(max = 500, message = "Voice URL must not exceed 500 characters")
    private String url;

    /** Voice note duration in seconds. */
    @Min(value = 0, message = "Voice duration must be zero or positive")
    private Integer durationSeconds;

    /** Original recorded file name. */
    @Size(max = 255, message = "Voice file name must not exceed 255 characters")
    private String fileName;

    /** MIME content type (e.g. audio/mp4, audio/mpeg). */
    @Size(max = 100, message = "Voice content type must not exceed 100 characters")
    private String contentType;
}

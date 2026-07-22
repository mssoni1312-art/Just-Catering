package com.justcatering.superadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Voice note metadata for a requirement list item.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementVoiceNoteResponse {

    /** Stored voice file URL. */
    private String url;

    /** Duration in seconds. */
    private Integer durationSeconds;

    /** Original recorded file name. */
    private String fileName;

    /** MIME content type. */
    private String contentType;
}

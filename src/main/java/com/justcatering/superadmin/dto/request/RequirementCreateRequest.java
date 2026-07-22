package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating an on-site requirement.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementCreateRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Requirement title. */
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    /** Requirement summary. */
    private String description;

    /** On-site notes. */
    @Size(max = 1000, message = "Notes must not exceed 1000 characters")
    private String notes;

    /** Assigned team member UUID. */
    private UUID assignedUserUuid;

    /** Check-in details. */
    @Valid
    private RequirementLocationRequest checkIn;

    /** Check-out details. */
    @Valid
    private RequirementLocationRequest checkOut;

    /**
     * Voice recording payload for create/update.
     * Example:
     * {
     *   "url": "/api/files/....m4a",
     *   "durationSeconds": 7,
     *   "fileName": "requirement_voice_....m4a",
     *   "contentType": "audio/mp4"
     * }
     */
    @Valid
    private RequirementVoiceRecordRequest voiceRecord;

    /**
     * Optional legacy voice URL.
     * Prefer {@link #voiceRecord} for POST/PUT.
     */
    @Size(max = 500, message = "Voice URL must not exceed 500 characters")
    private String voiceUrl;

    /**
     * Optional legacy voice duration.
     * Prefer {@link #voiceRecord} for POST/PUT.
     */
    @Min(value = 0, message = "Voice duration must be zero or positive")
    private Integer voiceDurationSeconds;

    /** Uploaded documents. */
    @Valid
    private List<RequirementDocumentRequest> documents;

    /** Optional entity status. */
    private EntityStatus status;
}

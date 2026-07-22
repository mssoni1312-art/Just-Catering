package com.justcatering.superadmin.dto.request;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for creating a client query / requirement.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientQueryCreateRequest {

    /** Related client UUID. */
    @NotNull(message = "Client is required")
    private UUID clientUuid;

    /** Query title. */
    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    /** Requirement summary shown below the title. */
    private String description;

    /** Optional query type. */
    @Size(max = 100, message = "Type must not exceed 100 characters")
    private String queryType;

    /** Assigned team member UUID. */
    private UUID assignedUserUuid;

    /** Department UUID. */
    private UUID departmentUuid;

    /** Priority level. */
    private Priority priority;

    /** Workflow status. */
    private QueryStatus queryStatus;

    /** Optional notes. */
    private String notes;

    /** Optional remarks (legacy alias for notes). */
    private String remarks;

    /** Optional image path. */
    @Size(max = 500, message = "Image URL must not exceed 500 characters")
    private String imageUrl;

    /** Scheduled requirement date and time. */
    private Instant dueDate;

    /** Scheduled requirement date and time (alias). */
    private Instant occurredAt;

    /** Whether check-in and check-out details are available. */
    private Boolean hasCheckInOut;

    /** Optional voice note URL. */
    @Size(max = 500, message = "Voice URL must not exceed 500 characters")
    private String voiceUrl;

    /** Voice note duration in seconds. */
    @Min(value = 0, message = "Voice duration must be zero or positive")
    private Integer voiceDurationSeconds;

    /** Optional uploaded document URL. */
    @Size(max = 500, message = "Document URL must not exceed 500 characters")
    private String documentUrl;

    /** Uploaded document display name. */
    @Size(max = 255, message = "Document name must not exceed 255 characters")
    private String documentName;

    /** Uploaded document size in bytes. */
    @Min(value = 0, message = "Document size must be zero or positive")
    private Long documentSizeBytes;

    /** Uploaded document MIME type. */
    @Size(max = 100, message = "Document content type must not exceed 100 characters")
    private String documentContentType;

    /** Completion timestamp. */
    private Instant completedAt;

    /** Optional entity status. */
    private EntityStatus status;
}

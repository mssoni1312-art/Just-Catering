package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * On-site requirement list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementListResponse {

    private UUID uuid;
    private String title;
    private String description;
    private String notes;
    private UUID clientUuid;
    private String clientName;
    private UUID assignedUserUuid;
    private String assignedUserName;
    private Instant occurredAt;
    private Boolean hasCheckInOut;
    private RequirementLocationResponse checkIn;
    private RequirementLocationResponse checkOut;
    private RequirementVoiceNoteResponse voiceNote;
    private List<RequirementDocumentResponse> documents;
    private RequirementDocumentResponse document;
    private EntityStatus status;
    private Instant createdAt;
}

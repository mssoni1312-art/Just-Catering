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
 * On-site requirement details response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementDetailsResponse {

    private UUID uuid;
    private String title;
    private String description;
    private String notes;
    private ClientDropdownResponse client;
    private UserDropdownResponse assignedUser;
    private Instant occurredAt;
    private Boolean hasCheckInOut;
    private RequirementLocationResponse checkIn;
    private RequirementLocationResponse checkOut;
    private RequirementVoiceNoteResponse voiceNote;
    private List<RequirementDocumentResponse> documents;
    private EntityStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}

package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Client query / requirement list item response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientQueryListResponse {

    private UUID uuid;
    private String title;
    private String description;
    private String notes;
    private String queryType;
    private UUID clientUuid;
    private String clientName;
    private String clientEmail;
    private String clientMobile;
    private String productName;
    private UUID assignedUserUuid;
    private String assignedUserName;
    private UUID departmentUuid;
    private String departmentName;
    private Priority priority;
    private QueryStatus queryStatus;
    private EntityStatus status;
    private Instant dueDate;
    private Instant occurredAt;
    private Boolean hasCheckInOut;
    private RequirementVoiceNoteResponse voiceNote;
    private RequirementDocumentResponse document;
    private Instant completedAt;
    private Instant createdAt;
}

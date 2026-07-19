package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight follow-up option for dropdowns.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowUpDropdownResponse {

    private UUID uuid;
    private String title;
    private String clientName;
    private FollowUpType followUpType;
    private FollowUpStatus followUpStatus;
    private LocalDate followUpDate;
}

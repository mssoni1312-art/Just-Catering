package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response after syncing manager assignments for a client.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerAssignmentSyncResponse {

    private UUID clientUuid;
    private String projectName;
    private LocalDate closeDate;
    private BigDecimal rewardAmount;
    private long totalCount;
    private List<ManagerAssignmentListResponse> assignments;
}

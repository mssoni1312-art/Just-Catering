package com.justcatering.superadmin.dto.response;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Check-in or check-out location response.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequirementLocationResponse {

    private Boolean enabled;
    private Instant dateTime;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
}

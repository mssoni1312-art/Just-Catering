package com.justcatering.superadmin.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight meeting lead option for dropdowns.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadDropdownResponse {

    private UUID uuid;
    private String ownerName;
    private String companyName;
    private String email;
}

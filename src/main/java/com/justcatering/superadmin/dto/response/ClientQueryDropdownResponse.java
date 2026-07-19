package com.justcatering.superadmin.dto.response;

import com.justcatering.superadmin.enums.Priority;
import com.justcatering.superadmin.enums.QueryStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Lightweight client query option for dropdowns.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientQueryDropdownResponse {

    private UUID uuid;
    private String title;
    private String clientName;
    private String queryType;
    private Priority priority;
    private QueryStatus queryStatus;
}

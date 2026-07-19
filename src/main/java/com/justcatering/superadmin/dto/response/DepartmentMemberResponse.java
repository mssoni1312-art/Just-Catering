package com.justcatering.superadmin.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Department member response used in department details.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentMemberResponse {

    private UUID uuid;
    private UUID userUuid;
    private String fullName;
    private String email;
    private String designation;
    private Boolean lead;
}

package com.justcatering.superadmin.dto.response;

import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Authenticated user summary returned with token responses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {

    /** Public user UUID. */
    private UUID uuid;

    /** First name. */
    private String firstName;

    /** Last name. */
    private String lastName;

    /** Full name. */
    private String fullName;

    /** Email address. */
    private String email;

    /** Phone number. */
    private String phone;

    /** Company name. */
    private String companyName;

    /** Profile image URL. */
    private String profileImageUrl;

    /** Assigned role codes. */
    private Set<String> roles;

    /** Effective permission codes. */
    private Set<String> permissions;
}

package com.justcatering.superadmin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Authentication token response containing access and refresh tokens.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthTokenResponse {

    /** JWT access token. */
    private String accessToken;

    /** Opaque refresh token. */
    private String refreshToken;

    /** Token type (always Bearer). */
    private String tokenType;

    /** Access token expiry in seconds. */
    private Long expiresIn;

    /** Authenticated user summary. */
    private UserSummaryResponse user;
}

package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.ChangePasswordRequest;
import com.justcatering.superadmin.dto.request.LoginRequest;
import com.justcatering.superadmin.dto.request.RefreshTokenRequest;
import com.justcatering.superadmin.dto.response.AuthTokenResponse;
import com.justcatering.superadmin.dto.response.UserSummaryResponse;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Authentication and session management service contract.
 */
public interface AuthService {

    /**
     * Authenticates a user and issues access/refresh tokens.
     *
     * @param request     login credentials
     * @param httpRequest HTTP request for device/IP metadata
     * @return token response
     */
    AuthTokenResponse login(LoginRequest request, HttpServletRequest httpRequest);

    /**
     * Rotates a refresh token and issues a new token pair.
     *
     * @param request     refresh token request
     * @param httpRequest HTTP request for device/IP metadata
     * @return token response
     */
    AuthTokenResponse refresh(RefreshTokenRequest request, HttpServletRequest httpRequest);

    /**
     * Revokes the provided refresh token (logout).
     *
     * @param request refresh token request
     */
    void logout(RefreshTokenRequest request);

    /**
     * Returns the currently authenticated user profile.
     *
     * @return user summary
     */
    UserSummaryResponse me();

    /**
     * Changes the password for the currently authenticated user.
     *
     * @param request change password payload
     */
    void changePassword(ChangePasswordRequest request);
}

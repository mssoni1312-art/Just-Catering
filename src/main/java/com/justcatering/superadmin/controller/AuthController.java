package com.justcatering.superadmin.controller;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.ChangePasswordRequest;
import com.justcatering.superadmin.dto.request.LoginRequest;
import com.justcatering.superadmin.dto.request.RefreshTokenRequest;
import com.justcatering.superadmin.dto.response.ApiResponse;
import com.justcatering.superadmin.dto.response.AuthTokenResponse;
import com.justcatering.superadmin.dto.response.UserSummaryResponse;
import com.justcatering.superadmin.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for authentication and session management.
 */
@RestController
@RequestMapping(AppConstants.AUTH_API)
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Login, token refresh, logout, and profile APIs")
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user and returns JWT tokens.
     *
     * @param request     login credentials
     * @param httpRequest HTTP request
     * @return token response
     */
    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate with email and password")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthTokenResponse data = authService.login(request, httpRequest);
        return ResponseEntity.ok(ApiResponse.success("Login successful", data, HttpStatus.OK.value()));
    }

    /**
     * Refreshes an access token using a valid refresh token.
     *
     * @param request     refresh token payload
     * @param httpRequest HTTP request
     * @return new token pair
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh token", description = "Rotate refresh token and issue new access token")
    public ResponseEntity<ApiResponse<AuthTokenResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request,
            HttpServletRequest httpRequest
    ) {
        AuthTokenResponse data = authService.refresh(request, httpRequest);
        return ResponseEntity.ok(
                ApiResponse.success("Token refreshed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Logs out by revoking the provided refresh token.
     *
     * @param request refresh token payload
     * @return success response
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Revoke the provided refresh token")
    public ResponseEntity<ApiResponse<Void>> logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request);
        return ResponseEntity.ok(ApiResponse.success("Logout successful", HttpStatus.OK.value()));
    }

    /**
     * Returns the currently authenticated user profile.
     *
     * @return user summary
     */
    @GetMapping("/me")
    @Operation(summary = "Current user", description = "Get the authenticated user profile")
    public ResponseEntity<ApiResponse<UserSummaryResponse>> me() {
        UserSummaryResponse data = authService.me();
        return ResponseEntity.ok(
                ApiResponse.success("Operation completed successfully", data, HttpStatus.OK.value())
        );
    }

    /**
     * Changes the password for the authenticated user.
     *
     * @param request change password payload
     * @return success response
     */
    @PutMapping("/change-password")
    @Operation(summary = "Change password", description = "Change password for the authenticated user")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        authService.changePassword(request);
        return ResponseEntity.ok(
                ApiResponse.success("Password changed successfully", HttpStatus.OK.value())
        );
    }
}

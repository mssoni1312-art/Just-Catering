package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.config.JwtProperties;
import com.justcatering.superadmin.config.SecurityProperties;
import com.justcatering.superadmin.dto.request.ChangePasswordRequest;
import com.justcatering.superadmin.dto.request.LoginRequest;
import com.justcatering.superadmin.dto.request.RefreshTokenRequest;
import com.justcatering.superadmin.dto.response.AuthTokenResponse;
import com.justcatering.superadmin.dto.response.UserSummaryResponse;
import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.entity.RefreshToken;
import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.AuthenticationException;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.UserMapper;
import com.justcatering.superadmin.repository.RefreshTokenRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.security.UserPrincipal;
import com.justcatering.superadmin.security.jwt.JwtTokenProvider;
import com.justcatering.superadmin.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link AuthService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final SecurityProperties securityProperties;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthTokenResponse login(LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmailWithRolesAndPermissions(request.getEmail().trim())
                .orElseThrow(() -> new AuthenticationException("Invalid email or password"));

        assertAccountUsable(user);

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            handleFailedLogin(user);
            throw new AuthenticationException("Invalid email or password");
        }

        resetFailedLoginState(user);
        user.setLastLoginAt(Instant.now());
        userRepository.save(user);

        log.info("User logged in successfully: {}", user.getEmail());
        return issueTokenPair(user, httpRequest);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthTokenResponse refresh(RefreshTokenRequest request, HttpServletRequest httpRequest) {
        RefreshToken existing = refreshTokenRepository
                .findByTokenAndDeletedFalse(request.getRefreshToken())
                .orElseThrow(() -> new AuthenticationException("Invalid or expired token"));

        if (!existing.isValid()) {
            existing.revoke(null);
            refreshTokenRepository.save(existing);
            throw new AuthenticationException("Invalid or expired token");
        }

        User user = userRepository
                .findActiveByIdWithRolesAndPermissions(existing.getUser().getId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new AuthenticationException("Account is inactive"));

        String replacement = UUID.randomUUID().toString();
        existing.revoke(replacement);
        refreshTokenRepository.save(existing);

        RefreshToken newRefresh = buildRefreshToken(user, replacement, httpRequest);
        refreshTokenRepository.save(newRefresh);

        return buildAuthResponse(user, newRefresh.getToken());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void logout(RefreshTokenRequest request) {
        refreshTokenRepository.findByTokenAndDeletedFalse(request.getRefreshToken())
                .ifPresent(token -> {
                    token.revoke(null);
                    token.setStatus(EntityStatus.REVOKED);
                    refreshTokenRepository.save(token);
                });
        SecurityContextHolder.clearContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserSummaryResponse me() {
        UserPrincipal principal = currentPrincipal();
        User user = userRepository
                .findActiveByIdWithRolesAndPermissions(principal.getId(), EntityStatus.ACTIVE)
                .orElseThrow(() -> new EntityNotFoundException("User", principal.getId()));
        return userMapper.toSummary(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changePassword(ChangePasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("New password and confirm password do not match");
        }

        UserPrincipal principal = currentPrincipal();
        User user = userRepository.findById(principal.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EntityNotFoundException("User", principal.getId()));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new AuthenticationException("Current password is incorrect");
        }

        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);
        refreshTokenRepository.revokeAllActiveByUserId(user.getId());

        log.info("Password changed for user: {}", user.getEmail());
    }

    private AuthTokenResponse issueTokenPair(User user, HttpServletRequest httpRequest) {
        RefreshToken refreshToken = buildRefreshToken(user, UUID.randomUUID().toString(), httpRequest);
        refreshTokenRepository.save(refreshToken);
        return buildAuthResponse(user, refreshToken.getToken());
    }

    private AuthTokenResponse buildAuthResponse(User user, String refreshTokenValue) {
        List<String> roles = user.getActiveRoles().stream().map(Role::getCode).toList();
        List<String> permissions = user.getActiveRoles().stream()
                .flatMap(role -> role.getActivePermissions().stream())
                .map(Permission::getCode)
                .distinct()
                .toList();

        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getUuid(),
                user.getEmail(),
                roles,
                permissions
        );

        return AuthTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshTokenValue)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenExpirySeconds())
                .user(userMapper.toSummary(user))
                .build();
    }

    private RefreshToken buildRefreshToken(User user, String tokenValue, HttpServletRequest httpRequest) {
        Instant expiresAt = Instant.now()
                .plus(jwtProperties.getRefreshTokenExpirationMs(), ChronoUnit.MILLIS);

        return RefreshToken.builder()
                .user(user)
                .token(tokenValue)
                .expiresAt(expiresAt)
                .revoked(Boolean.FALSE)
                .deviceInfo(truncate(httpRequest.getHeader("User-Agent"), 255))
                .ipAddress(resolveClientIp(httpRequest))
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();
    }

    private void assertAccountUsable(User user) {
        if (user.isAccountLocked()) {
            throw new AuthenticationException(
                    "Account is temporarily locked due to multiple failed login attempts"
            );
        }
        if (user.getStatus() != EntityStatus.ACTIVE || user.isDeleted()) {
            throw new AuthenticationException("Account is inactive");
        }
    }

    private void handleFailedLogin(User user) {
        int attempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
        attempts++;
        user.setFailedLoginAttempts(attempts);

        if (attempts >= securityProperties.getMaxFailedLoginAttempts()) {
            user.setStatus(EntityStatus.LOCKED);
            user.setLockedUntil(Instant.now()
                    .plus(securityProperties.getLockDurationMinutes(), ChronoUnit.MINUTES));
            log.warn("Account locked for user {} after {} failed attempts",
                    user.getEmail(), attempts);
        }
        userRepository.save(user);
    }

    private void resetFailedLoginState(User user) {
        user.setFailedLoginAttempts(0);
        user.setLockedUntil(null);
        if (user.getStatus() == EntityStatus.LOCKED) {
            user.setStatus(EntityStatus.ACTIVE);
        }
    }

    private UserPrincipal currentPrincipal() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UserPrincipal principal)) {
            throw new AuthenticationException("Unauthorized access");
        }
        return principal;
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(forwarded)) {
            return truncate(forwarded.split(",")[0].trim(), 45);
        }
        return truncate(request.getRemoteAddr(), 45);
    }

    private String truncate(String value, int max) {
        if (value == null) {
            return null;
        }
        return value.length() <= max ? value : value.substring(0, max);
    }
}

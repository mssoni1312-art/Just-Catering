package com.justcatering.superadmin.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Refresh token entity used for JWT session renewal.
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity {

    /** Owning user. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /** Opaque refresh token value. */
    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

    /** Absolute expiry timestamp. */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /** Whether the token has been revoked. */
    @Column(name = "revoked", nullable = false)
    @Builder.Default
    private Boolean revoked = Boolean.FALSE;

    /** Timestamp when the token was revoked. */
    @Column(name = "revoked_at")
    private Instant revokedAt;

    /** Token that replaced this one during rotation. */
    @Column(name = "replaced_by_token", length = 500)
    private String replacedByToken;

    /** Optional client / device metadata. */
    @Column(name = "device_info", length = 255)
    private String deviceInfo;

    /** Client IP address. */
    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    /**
     * Returns whether the refresh token is still usable.
     *
     * @return {@code true} if not revoked, not deleted, and not expired
     */
    public boolean isValid() {
        return !Boolean.TRUE.equals(revoked)
                && !isDeleted()
                && Instant.now().isBefore(expiresAt);
    }

    /**
     * Revokes this refresh token.
     *
     * @param replacedBy optional replacement token value
     */
    public void revoke(String replacedBy) {
        this.revoked = Boolean.TRUE;
        this.revokedAt = Instant.now();
        this.replacedByToken = replacedBy;
    }
}

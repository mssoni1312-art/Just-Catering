package com.justcatering.superadmin.entity;

import com.justcatering.superadmin.enums.EntityStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Application user entity representing Super Admin and staff accounts.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    /** User first name. */
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    /** User last name. */
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    /** Unique login email. */
    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    /** BCrypt-hashed password. */
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    /** Optional phone number. */
    @Column(name = "phone", length = 20)
    private String phone;

    /** Optional company name. */
    @Column(name = "company_name", length = 255)
    private String companyName;

    /** Optional profile image URL. */
    @Column(name = "profile_image_url", length = 500)
    private String profileImageUrl;

    /** Whether the email address has been verified. */
    @Column(name = "email_verified", nullable = false)
    @Builder.Default
    private Boolean emailVerified = Boolean.FALSE;

    /** Timestamp of the last successful login. */
    @Column(name = "last_login_at")
    private Instant lastLoginAt;

    /** Consecutive failed login attempts. */
    @Column(name = "failed_login_attempts", nullable = false)
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    /** Account lock expiry after failed attempts. */
    @Column(name = "locked_until")
    private Instant lockedUntil;

    /** Timestamp when the password was last changed. */
    @Column(name = "password_changed_at")
    private Instant passwordChangedAt;

    /** User-role associations. */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserRole> userRoles = new HashSet<>();

    /**
     * Returns the user's full display name.
     *
     * @return concatenated first and last name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Returns active roles assigned to this user.
     *
     * @return set of active roles
     */
    public Set<Role> getActiveRoles() {
        return userRoles.stream()
                .filter(ur -> !ur.isDeleted())
                .map(UserRole::getRole)
                .filter(role -> !role.isDeleted())
                .collect(Collectors.toSet());
    }

    /**
     * Returns whether the account is currently locked.
     *
     * @return {@code true} if locked and lock has not expired
     */
    public boolean isAccountLocked() {
        if (getStatus() == EntityStatus.LOCKED) {
            return lockedUntil == null || Instant.now().isBefore(lockedUntil);
        }
        return lockedUntil != null && Instant.now().isBefore(lockedUntil);
    }

    /**
     * Returns whether the account can authenticate.
     *
     * @return {@code true} if active, not deleted, and not locked
     */
    public boolean isAuthenticatable() {
        return !isDeleted()
                && getStatus() == EntityStatus.ACTIVE
                && !isAccountLocked();
    }
}

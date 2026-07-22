package com.justcatering.superadmin.repository;

import com.justcatering.superadmin.entity.RefreshToken;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link RefreshToken} persistence operations.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Finds a non-deleted refresh token by token value.
     *
     * @param token token value
     * @return optional refresh token
     */
    @Query("""
            SELECT rt FROM RefreshToken rt
            JOIN FETCH rt.user u
            WHERE rt.token = :token
              AND rt.deleted = FALSE
            """)
    Optional<RefreshToken> findByTokenAndDeletedFalse(@Param("token") String token);

    /**
     * Revokes all active refresh tokens for a user at the current instant.
     *
     * @param userId user id
     * @return number of updated rows
     */
    default int revokeAllActiveByUserId(Long userId) {
        return revokeAllActiveByUserId(userId, Instant.now());
    }

    @Modifying(clearAutomatically = true)
    @Query("""
            UPDATE RefreshToken rt
            SET rt.revoked = TRUE,
                rt.revokedAt = :revokedAt,
                rt.status = com.justcatering.superadmin.enums.EntityStatus.REVOKED
            WHERE rt.user.id = :userId
              AND rt.revoked = FALSE
              AND rt.deleted = FALSE
            """)
    int revokeAllActiveByUserId(@Param("userId") Long userId, @Param("revokedAt") Instant revokedAt);
}

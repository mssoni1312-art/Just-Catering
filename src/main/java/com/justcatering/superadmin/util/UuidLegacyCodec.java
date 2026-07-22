package com.justcatering.superadmin.util;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Helpers for legacy SQLite role UUID values that were exposed to clients as
 * hex-encoded ASCII strings.
 */
public final class UuidLegacyCodec {

    private UuidLegacyCodec() {
    }

    /**
     * Converts a canonical UUID string into the legacy corrupted representation
     * previously returned by the SQLite API.
     *
     * @param canonicalUuid canonical UUID string
     * @return legacy corrupted UUID string
     */
    public static String toLegacyCorruptedUuid(String canonicalUuid) {
        StringBuilder encoded = new StringBuilder(canonicalUuid.length() * 2);
        for (int index = 0; index < canonicalUuid.length(); index++) {
            encoded.append(String.format(Locale.ROOT, "%02x", (int) canonicalUuid.charAt(index)));
        }

        if (encoded.length() < 32) {
            return encoded.toString();
        }

        return String.format(
                Locale.ROOT,
                "%s-%s-%s-%s-%s",
                encoded.substring(0, 8),
                encoded.substring(8, 12),
                encoded.substring(12, 16),
                encoded.substring(16, 20),
                encoded.substring(20, 32)
        );
    }

    /**
     * Returns whether the supplied value matches the legacy corrupted form of
     * the provided canonical UUID.
     *
     * @param identifier    incoming identifier
     * @param canonicalUuid canonical UUID
     * @return {@code true} when the identifier is a legacy match
     */
    public static boolean matchesLegacyCorruptedUuid(String identifier, UUID canonicalUuid) {
        if (identifier == null || canonicalUuid == null) {
            return false;
        }
        String canonical = canonicalUuid.toString();
        return identifier.equalsIgnoreCase(canonical)
                || identifier.equalsIgnoreCase(toLegacyCorruptedUuid(canonical));
    }

    /**
     * Decodes a legacy corrupted UUID string back to its canonical form.
     *
     * @param identifier legacy or canonical UUID string
     * @return canonical UUID when decodable
     */
    public static Optional<UUID> fromLegacyCorruptedUuid(String identifier) {
        if (identifier == null || identifier.isBlank()) {
            return Optional.empty();
        }

        String hex = identifier.replace("-", "");
        if (hex.length() % 2 != 0) {
            return Optional.empty();
        }

        StringBuilder decoded = new StringBuilder(hex.length() / 2);
        for (int index = 0; index < hex.length(); index += 2) {
            decoded.append((char) Integer.parseInt(hex.substring(index, index + 2), 16));
        }

        try {
            String canonical = decoded.toString();
            UUID uuid = UUID.fromString(canonical);
            if (identifier.equalsIgnoreCase(toLegacyCorruptedUuid(canonical))) {
                return Optional.of(uuid);
            }
            return Optional.empty();
        } catch (IllegalArgumentException ignored) {
            return Optional.empty();
        }
    }
}

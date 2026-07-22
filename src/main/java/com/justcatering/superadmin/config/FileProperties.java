package com.justcatering.superadmin.config;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * File storage configuration properties bound from {@code app.file.*}.
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.file")
public class FileProperties {

    /** Directory where uploaded files are stored. */
    private String uploadDir = "./uploads";

    /** Maximum allowed file size in bytes. */
    private long maxSizeBytes = 10_485_760L;

    /** Comma-separated allowed image MIME types. */
    private String allowedImageTypes = "image/jpeg,image/png,image/webp,image/gif";

    /** Comma-separated allowed document MIME types. */
    private String allowedDocumentTypes =
            "application/pdf,application/msword,"
                    + "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    /** Comma-separated allowed audio MIME types for voice recordings. */
    private String allowedAudioTypes =
            "audio/mpeg,audio/mp4,audio/x-m4a,audio/m4a,audio/aac,audio/wav,audio/x-wav,"
                    + "audio/webm,audio/ogg,audio/x-caf,audio/caf";

    /**
     * Returns all allowed MIME types (images, documents, and audio).
     *
     * @return allowed content types
     */
    public Set<String> getAllowedContentTypes() {
        return Arrays.stream(
                        (allowedImageTypes + "," + allowedDocumentTypes + "," + allowedAudioTypes).split(",")
                )
                .map(String::trim)
                .filter(type -> !type.isEmpty())
                .collect(Collectors.toUnmodifiableSet());
    }
}

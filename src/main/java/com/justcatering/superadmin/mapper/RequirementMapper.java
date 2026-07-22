package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.RequirementDetailsResponse;
import com.justcatering.superadmin.dto.response.RequirementDocumentResponse;
import com.justcatering.superadmin.dto.response.RequirementListResponse;
import com.justcatering.superadmin.dto.response.RequirementLocationResponse;
import com.justcatering.superadmin.dto.response.RequirementVoiceNoteResponse;
import com.justcatering.superadmin.entity.Requirement;
import com.justcatering.superadmin.entity.RequirementDocument;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

/**
 * MapStruct mapper for {@link Requirement} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class})
public interface RequirementMapper {

    /**
     * Maps a requirement to a list response.
     *
     * @param requirement requirement entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "assignedUserUuid", source = "assignedUser.uuid")
    @Mapping(
            target = "assignedUserName",
            expression = "java(requirement.getAssignedUser() != null ? requirement.getAssignedUser().getFullName() : null)"
    )
    @Mapping(target = "occurredAt", expression = "java(resolveOccurredAt(requirement))")
    @Mapping(target = "hasCheckInOut", expression = "java(hasCheckInOut(requirement))")
    @Mapping(target = "checkIn", expression = "java(toCheckIn(requirement))")
    @Mapping(target = "checkOut", expression = "java(toCheckOut(requirement))")
    @Mapping(target = "voiceNote", expression = "java(toVoiceNote(requirement))")
    @Mapping(target = "documents", expression = "java(toDocuments(requirement))")
    @Mapping(target = "document", expression = "java(toPrimaryDocument(requirement))")
    RequirementListResponse toList(Requirement requirement);

    /**
     * Maps a requirement to a details response.
     *
     * @param requirement requirement entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "assignedUser", source = "assignedUser")
    @Mapping(target = "occurredAt", expression = "java(resolveOccurredAt(requirement))")
    @Mapping(target = "hasCheckInOut", expression = "java(hasCheckInOut(requirement))")
    @Mapping(target = "checkIn", expression = "java(toCheckIn(requirement))")
    @Mapping(target = "checkOut", expression = "java(toCheckOut(requirement))")
    @Mapping(target = "voiceNote", expression = "java(toVoiceNote(requirement))")
    @Mapping(target = "documents", expression = "java(toDocuments(requirement))")
    RequirementDetailsResponse toDetails(Requirement requirement);

    /**
     * Resolves the display date/time for list cards.
     *
     * @param requirement requirement entity
     * @return occurred instant
     */
    default Instant resolveOccurredAt(Requirement requirement) {
        if (requirement == null) {
            return null;
        }
        if (requirement.getCheckInAt() != null) {
            return requirement.getCheckInAt();
        }
        return requirement.getCreatedAt();
    }

    /**
     * Returns whether check-in or check-out is available.
     *
     * @param requirement requirement entity
     * @return {@code true} when either location block is enabled
     */
    default Boolean hasCheckInOut(Requirement requirement) {
        if (requirement == null) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE.equals(requirement.getCheckInEnabled())
                || Boolean.TRUE.equals(requirement.getCheckOutEnabled());
    }

    /**
     * Maps check-in location details.
     *
     * @param requirement requirement entity
     * @return check-in response
     */
    default RequirementLocationResponse toCheckIn(Requirement requirement) {
        if (requirement == null || !Boolean.TRUE.equals(requirement.getCheckInEnabled())) {
            return null;
        }
        return RequirementLocationResponse.builder()
                .enabled(requirement.getCheckInEnabled())
                .dateTime(requirement.getCheckInAt())
                .latitude(requirement.getCheckInLatitude())
                .longitude(requirement.getCheckInLongitude())
                .address(requirement.getCheckInAddress())
                .build();
    }

    /**
     * Maps check-out location details.
     *
     * @param requirement requirement entity
     * @return check-out response
     */
    default RequirementLocationResponse toCheckOut(Requirement requirement) {
        if (requirement == null || !Boolean.TRUE.equals(requirement.getCheckOutEnabled())) {
            return null;
        }
        return RequirementLocationResponse.builder()
                .enabled(requirement.getCheckOutEnabled())
                .dateTime(requirement.getCheckOutAt())
                .latitude(requirement.getCheckOutLatitude())
                .longitude(requirement.getCheckOutLongitude())
                .address(requirement.getCheckOutAddress())
                .build();
    }

    /**
     * Maps voice note metadata when present.
     *
     * @param requirement requirement entity
     * @return voice note response or {@code null}
     */
    default RequirementVoiceNoteResponse toVoiceNote(Requirement requirement) {
        if (requirement == null) {
            return null;
        }

        boolean hasUrl = StringUtils.hasText(requirement.getVoiceUrl());
        Integer durationSeconds = requirement.getVoiceDurationSeconds();
        boolean hasDuration = durationSeconds != null && durationSeconds > 0;
        if (!hasUrl && !hasDuration) {
            return null;
        }

        return RequirementVoiceNoteResponse.builder()
                .url(hasUrl ? requirement.getVoiceUrl().trim() : null)
                .durationSeconds(durationSeconds)
                .fileName(StringUtils.hasText(requirement.getVoiceFileName())
                        ? requirement.getVoiceFileName().trim() : null)
                .contentType(StringUtils.hasText(requirement.getVoiceContentType())
                        ? requirement.getVoiceContentType().trim() : null)
                .build();
    }

    /**
     * Maps uploaded documents.
     *
     * @param requirement requirement entity
     * @return document responses
     */
    default List<RequirementDocumentResponse> toDocuments(Requirement requirement) {
        if (requirement == null || requirement.getDocuments() == null) {
            return List.of();
        }
        return requirement.getDocuments().stream()
                .filter(document -> !Boolean.TRUE.equals(document.getDeleted()))
                .map(this::toDocument)
                .toList();
    }

    /**
     * Maps the first uploaded document for compact list cards.
     *
     * @param requirement requirement entity
     * @return primary document or {@code null}
     */
    default RequirementDocumentResponse toPrimaryDocument(Requirement requirement) {
        List<RequirementDocumentResponse> documents = toDocuments(requirement);
        return documents.isEmpty() ? null : documents.get(0);
    }

    /**
     * Maps a requirement document entity.
     *
     * @param document document entity
     * @return document response
     */
    default RequirementDocumentResponse toDocument(RequirementDocument document) {
        if (document == null) {
            return null;
        }
        return RequirementDocumentResponse.builder()
                .fileName(document.getFileName())
                .contentType(document.getContentType())
                .sizeBytes(document.getFileSizeBytes())
                .url(document.getFileUrl())
                .meta(buildDocumentMeta(
                        document.getFileSizeBytes(),
                        document.getContentType(),
                        document.getFileName()
                ))
                .build();
    }

    /**
     * Builds a display label such as {@code 1.2 MB • PDF}.
     *
     * @param sizeBytes   file size
     * @param contentType MIME type
     * @param fileName    original file name
     * @return display meta
     */
    default String buildDocumentMeta(Long sizeBytes, String contentType, String fileName) {
        String sizeLabel = formatFileSize(sizeBytes);
        String typeLabel = resolveDocumentTypeLabel(contentType, fileName);
        if (!StringUtils.hasText(sizeLabel)) {
            return typeLabel;
        }
        if (!StringUtils.hasText(typeLabel)) {
            return sizeLabel;
        }
        return sizeLabel + " • " + typeLabel;
    }

    /**
     * Formats a byte size into a compact label.
     *
     * @param sizeBytes file size
     * @return formatted size
     */
    default String formatFileSize(Long sizeBytes) {
        if (sizeBytes == null || sizeBytes < 0) {
            return null;
        }
        if (sizeBytes < 1024) {
            return sizeBytes + " B";
        }

        double size = sizeBytes;
        String[] units = {"KB", "MB", "GB"};
        int unitIndex = -1;
        do {
            size /= 1024;
            unitIndex++;
        } while (size >= 1024 && unitIndex < units.length - 1);

        return String.format(java.util.Locale.US, "%.1f %s", size, units[unitIndex]);
    }

    /**
     * Resolves a short document type label from MIME type or file extension.
     *
     * @param contentType MIME type
     * @param fileName    file name
     * @return short type label
     */
    default String resolveDocumentTypeLabel(String contentType, String fileName) {
        if (StringUtils.hasText(contentType)) {
            return switch (contentType.toLowerCase(java.util.Locale.ROOT)) {
                case "application/pdf" -> "PDF";
                case "application/msword" -> "DOC";
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "DOCX";
                case "image/jpeg", "image/jpg" -> "JPG";
                case "image/png" -> "PNG";
                default -> contentType.substring(contentType.lastIndexOf('/') + 1).toUpperCase(java.util.Locale.ROOT);
            };
        }

        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase(java.util.Locale.ROOT);
    }
}

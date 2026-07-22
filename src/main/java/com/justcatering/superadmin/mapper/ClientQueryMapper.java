package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ClientQueryDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientQueryDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientQueryListResponse;
import com.justcatering.superadmin.dto.response.RequirementDocumentResponse;
import com.justcatering.superadmin.dto.response.RequirementVoiceNoteResponse;
import com.justcatering.superadmin.entity.ClientQuery;
import java.time.Instant;
import java.util.Locale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.util.StringUtils;

/**
 * MapStruct mapper for {@link ClientQuery} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class, DepartmentMapper.class})
public interface ClientQueryMapper {

    /**
     * Maps a client query to a list response.
     *
     * @param clientQuery client query entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "clientEmail", source = "client.email")
    @Mapping(target = "clientMobile", source = "client.mobile")
    @Mapping(target = "productName", source = "client.product.name")
    @Mapping(target = "assignedUserUuid", source = "assignedUser.uuid")
    @Mapping(
            target = "assignedUserName",
            expression = "java(clientQuery.getAssignedUser() != null ? clientQuery.getAssignedUser().getFullName() : null)"
    )
    @Mapping(target = "departmentUuid", source = "department.uuid")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "notes", source = "remarks")
    @Mapping(target = "dueDate", expression = "java(resolveScheduledAt(clientQuery))")
    @Mapping(target = "occurredAt", expression = "java(resolveScheduledAt(clientQuery))")
    @Mapping(target = "voiceNote", expression = "java(toVoiceNote(clientQuery))")
    @Mapping(target = "document", expression = "java(toDocument(clientQuery))")
    ClientQueryListResponse toList(ClientQuery clientQuery);

    /**
     * Maps a client query to a details response.
     *
     * @param clientQuery client query entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "assignedUser", source = "assignedUser")
    @Mapping(target = "department", source = "department")
    @Mapping(target = "notes", source = "remarks")
    @Mapping(target = "dueDate", expression = "java(resolveScheduledAt(clientQuery))")
    @Mapping(target = "occurredAt", expression = "java(resolveScheduledAt(clientQuery))")
    @Mapping(target = "voiceNote", expression = "java(toVoiceNote(clientQuery))")
    @Mapping(target = "document", expression = "java(toDocument(clientQuery))")
    ClientQueryDetailsResponse toDetails(ClientQuery clientQuery);

    /**
     * Maps a client query to a dropdown response.
     *
     * @param clientQuery client query entity
     * @return dropdown response
     */
    @Mapping(target = "clientName", source = "client.clientName")
    ClientQueryDropdownResponse toDropdown(ClientQuery clientQuery);

    /**
     * Resolves the scheduled date/time for a requirement.
     *
     * @param clientQuery client query entity
     * @return scheduled instant, falling back to createdAt
     */
    default Instant resolveScheduledAt(ClientQuery clientQuery) {
        if (clientQuery == null) {
            return null;
        }
        return clientQuery.getScheduledAt() != null
                ? clientQuery.getScheduledAt()
                : clientQuery.getCreatedAt();
    }

    /**
     * Maps voice note metadata when present.
     *
     * @param clientQuery client query entity
     * @return voice note response or {@code null}
     */
    default RequirementVoiceNoteResponse toVoiceNote(ClientQuery clientQuery) {
        if (clientQuery == null
                || !StringUtils.hasText(clientQuery.getVoiceUrl())
                || clientQuery.getVoiceDurationSeconds() == null) {
            return null;
        }

        return RequirementVoiceNoteResponse.builder()
                .url(clientQuery.getVoiceUrl())
                .durationSeconds(clientQuery.getVoiceDurationSeconds())
                .build();
    }

    /**
     * Maps uploaded document metadata when present.
     *
     * @param clientQuery client query entity
     * @return document response or {@code null}
     */
    default RequirementDocumentResponse toDocument(ClientQuery clientQuery) {
        if (clientQuery == null || !StringUtils.hasText(clientQuery.getDocumentUrl())) {
            return null;
        }

        return RequirementDocumentResponse.builder()
                .fileName(clientQuery.getDocumentName())
                .contentType(clientQuery.getDocumentContentType())
                .sizeBytes(clientQuery.getDocumentSizeBytes())
                .url(clientQuery.getDocumentUrl())
                .meta(buildDocumentMeta(
                        clientQuery.getDocumentSizeBytes(),
                        clientQuery.getDocumentContentType(),
                        clientQuery.getDocumentName()
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

        return String.format(Locale.US, "%.1f %s", size, units[unitIndex]);
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
            return switch (contentType.toLowerCase(Locale.ROOT)) {
                case "application/pdf" -> "PDF";
                case "application/msword" -> "DOC";
                case "application/vnd.openxmlformats-officedocument.wordprocessingml.document" -> "DOCX";
                case "image/jpeg", "image/jpg" -> "JPG";
                case "image/png" -> "PNG";
                default -> contentType.substring(contentType.lastIndexOf('/') + 1).toUpperCase(Locale.ROOT);
            };
        }

        if (!StringUtils.hasText(fileName) || !fileName.contains(".")) {
            return null;
        }
        return fileName.substring(fileName.lastIndexOf('.') + 1).toUpperCase(Locale.ROOT);
    }
}

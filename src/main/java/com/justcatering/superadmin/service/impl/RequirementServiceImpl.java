package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.RequirementCreateRequest;
import com.justcatering.superadmin.dto.request.RequirementDocumentRequest;
import com.justcatering.superadmin.dto.request.RequirementLocationRequest;
import com.justcatering.superadmin.dto.request.RequirementVoiceRecordRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.RequirementDetailsResponse;
import com.justcatering.superadmin.dto.response.RequirementListResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.Requirement;
import com.justcatering.superadmin.entity.RequirementDocument;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.RequirementMapper;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.RequirementRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.RequirementService;
import com.justcatering.superadmin.specification.RequirementSpecification;
import com.justcatering.superadmin.util.UuidLegacyCodec;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link RequirementService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RequirementServiceImpl implements RequirementService {

    private static final int MAX_VOICE_DURATION_SECONDS = 120;
    private static final long MAX_DOCUMENT_SIZE_BYTES = 10L * 1024 * 1024;

    private final RequirementRepository requirementRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final RequirementMapper requirementMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public RequirementDetailsResponse create(RequirementCreateRequest request) {
        validateDocuments(request.getDocuments());

        Requirement requirement = Requirement.builder()
                .client(resolveActiveClient(request.getClientUuid()))
                .title(request.getTitle().trim())
                .description(normalizeOptional(request.getDescription()))
                .notes(normalizeOptional(request.getNotes()))
                .assignedUser(resolveOptionalUser(request.getAssignedUserUuid()))
                .checkInEnabled(isEnabled(request.getCheckIn()))
                .checkInAt(request.getCheckIn() != null ? request.getCheckIn().getDateTime() : null)
                .checkInLatitude(request.getCheckIn() != null ? request.getCheckIn().getLatitude() : null)
                .checkInLongitude(request.getCheckIn() != null ? request.getCheckIn().getLongitude() : null)
                .checkInAddress(request.getCheckIn() != null
                        ? normalizeOptional(request.getCheckIn().getAddress()) : null)
                .checkOutEnabled(isEnabled(request.getCheckOut()))
                .checkOutAt(request.getCheckOut() != null ? request.getCheckOut().getDateTime() : null)
                .checkOutLatitude(request.getCheckOut() != null ? request.getCheckOut().getLatitude() : null)
                .checkOutLongitude(request.getCheckOut() != null ? request.getCheckOut().getLongitude() : null)
                .checkOutAddress(request.getCheckOut() != null
                        ? normalizeOptional(request.getCheckOut().getAddress()) : null)
                .documents(new ArrayList<>())
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        applyVoiceRecord(requirement, request, false);
        requirement.getDocuments().addAll(buildDocuments(requirement, request.getDocuments()));
        validateLocation(requirement.getCheckInEnabled(), requirement.getCheckInLatitude(),
                requirement.getCheckInLongitude(), "Check-in");
        validateLocation(requirement.getCheckOutEnabled(), requirement.getCheckOutLatitude(),
                requirement.getCheckOutLongitude(), "Check-out");

        requirement = requirementRepository.save(requirement);
        log.info("Created requirement: {}", requirement.getTitle());
        return requirementMapper.toDetails(reloadWithRelations(requirement.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public RequirementDetailsResponse getByUuid(UUID uuid) {
        return requirementMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<RequirementListResponse> search(
            String search,
            UUID clientUuid,
            UUID assignedUserUuid,
            Pageable pageable
    ) {
        Page<Requirement> page = requirementRepository.findAll(
                RequirementSpecification.filter(search, clientUuid, assignedUserUuid),
                pageable
        );
        return PageResponse.from(page.map(requirementMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RequirementDetailsResponse update(UUID uuid, RequirementCreateRequest request) {
        validateDocuments(request.getDocuments());

        Requirement requirement = findOrThrow(uuid);
        requirement.setClient(resolveActiveClient(request.getClientUuid()));
        requirement.setTitle(request.getTitle().trim());
        requirement.setDescription(normalizeOptional(request.getDescription()));
        requirement.setNotes(normalizeOptional(request.getNotes()));
        requirement.setAssignedUser(resolveOptionalUser(request.getAssignedUserUuid()));
        requirement.setCheckInEnabled(isEnabled(request.getCheckIn()));
        requirement.setCheckInAt(request.getCheckIn() != null ? request.getCheckIn().getDateTime() : null);
        requirement.setCheckInLatitude(request.getCheckIn() != null ? request.getCheckIn().getLatitude() : null);
        requirement.setCheckInLongitude(request.getCheckIn() != null ? request.getCheckIn().getLongitude() : null);
        requirement.setCheckInAddress(request.getCheckIn() != null
                ? normalizeOptional(request.getCheckIn().getAddress()) : null);
        requirement.setCheckOutEnabled(isEnabled(request.getCheckOut()));
        requirement.setCheckOutAt(request.getCheckOut() != null ? request.getCheckOut().getDateTime() : null);
        requirement.setCheckOutLatitude(request.getCheckOut() != null ? request.getCheckOut().getLatitude() : null);
        requirement.setCheckOutLongitude(request.getCheckOut() != null ? request.getCheckOut().getLongitude() : null);
        requirement.setCheckOutAddress(request.getCheckOut() != null
                ? normalizeOptional(request.getCheckOut().getAddress()) : null);
        applyVoiceRecord(requirement, request, true);
        if (request.getStatus() != null) {
            requirement.setStatus(request.getStatus());
        }

        requirement.getDocuments().clear();
        requirement.getDocuments().addAll(buildDocuments(requirement, request.getDocuments()));

        validateLocation(requirement.getCheckInEnabled(), requirement.getCheckInLatitude(),
                requirement.getCheckInLongitude(), "Check-in");
        validateLocation(requirement.getCheckOutEnabled(), requirement.getCheckOutLatitude(),
                requirement.getCheckOutLongitude(), "Check-out");

        requirementRepository.save(requirement);
        log.info("Updated requirement: {}", requirement.getTitle());
        return requirementMapper.toDetails(reloadWithRelations(requirement.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        Requirement requirement = findOrThrow(uuid);
        requirement.softDelete();
        requirementRepository.save(requirement);
        log.info("Soft-deleted requirement: {}", requirement.getTitle());
    }

    private Requirement findOrThrow(UUID uuid) {
        return findByResolvedUuid(uuid)
                .orElseThrow(() -> new EntityNotFoundException("Requirement", uuid));
    }

    private Optional<Requirement> findByResolvedUuid(UUID uuid) {
        Optional<Requirement> found = requirementRepository.findByUuidAndDeletedFalse(uuid);
        if (found.isPresent()) {
            return found;
        }

        String identifier = uuid.toString();
        return requirementRepository.findByDeletedFalse().stream()
                .filter(requirement -> UuidLegacyCodec.matchesLegacyCorruptedUuid(
                        identifier,
                        requirement.getUuid()
                ))
                .findFirst();
    }

    private Client resolveActiveClient(UUID clientUuid) {
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected client is not active");
        }
        return client;
    }

    private User resolveOptionalUser(UUID userUuid) {
        if (userUuid == null) {
            return null;
        }
        User user = userRepository.findByUuidAndDeletedFalse(userUuid)
                .orElseThrow(() -> new EntityNotFoundException("User", userUuid));
        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected user is not active");
        }
        return user;
    }

    private Requirement reloadWithRelations(UUID uuid) {
        Requirement requirement = findOrThrow(uuid);
        Requirement loaded = requirementRepository.findByUuidWithRelations(requirement.getUuid())
                .orElseThrow(() -> new EntityNotFoundException("Requirement", uuid));
        loaded.getDocuments().size();
        return loaded;
    }

    private void applyVoiceRecord(
            Requirement requirement,
            RequirementCreateRequest request,
            boolean preserveExistingWhenBlank
    ) {
        RequirementVoiceRecordRequest voiceRecord = request.getVoiceRecord();

        String url = voiceRecord != null && StringUtils.hasText(voiceRecord.getUrl())
                ? voiceRecord.getUrl().trim()
                : normalizeOptional(request.getVoiceUrl());
        Integer durationSeconds = voiceRecord != null && voiceRecord.getDurationSeconds() != null
                ? voiceRecord.getDurationSeconds()
                : request.getVoiceDurationSeconds();
        String fileName = voiceRecord != null
                ? normalizeOptional(voiceRecord.getFileName())
                : null;
        String contentType = voiceRecord != null
                ? normalizeOptional(voiceRecord.getContentType())
                : null;

        validateVoiceDuration(durationSeconds);

        if (preserveExistingWhenBlank && !StringUtils.hasText(url) && StringUtils.hasText(requirement.getVoiceUrl())) {
            // Keep stored recording when client only refreshes duration/metadata without a new URL.
            if (durationSeconds != null) {
                requirement.setVoiceDurationSeconds(durationSeconds);
            }
            if (fileName != null) {
                requirement.setVoiceFileName(fileName);
            }
            if (contentType != null) {
                requirement.setVoiceContentType(contentType);
            }
            return;
        }

        requirement.setVoiceUrl(url);
        requirement.setVoiceDurationSeconds(durationSeconds);
        requirement.setVoiceFileName(fileName);
        requirement.setVoiceContentType(contentType);
    }

    private List<RequirementDocument> buildDocuments(
            Requirement requirement,
            List<RequirementDocumentRequest> documents
    ) {
        if (documents == null || documents.isEmpty()) {
            return List.of();
        }

        return documents.stream()
                .map(document -> RequirementDocument.builder()
                        .requirement(requirement)
                        .fileName(document.getFileName().trim())
                        .fileUrl(document.getUrl().trim())
                        .fileSizeBytes(document.getSizeBytes())
                        .contentType(normalizeOptional(document.getContentType()))
                        .status(EntityStatus.ACTIVE)
                        .deleted(Boolean.FALSE)
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void validateVoiceDuration(Integer voiceDurationSeconds) {
        if (voiceDurationSeconds != null && voiceDurationSeconds > MAX_VOICE_DURATION_SECONDS) {
            throw new BusinessException(
                    "Voice note duration must not exceed 120 seconds",
                    "VOICE_DURATION_EXCEEDED"
            );
        }
    }

    private void validateDocuments(List<RequirementDocumentRequest> documents) {
        if (documents == null) {
            return;
        }
        for (RequirementDocumentRequest document : documents) {
            if (document.getSizeBytes() != null && document.getSizeBytes() > MAX_DOCUMENT_SIZE_BYTES) {
                throw new BusinessException(
                        "Document size must not exceed 10 MB",
                        "DOCUMENT_SIZE_EXCEEDED"
                );
            }
        }
    }

    private void validateLocation(
            boolean enabled,
            java.math.BigDecimal latitude,
            java.math.BigDecimal longitude,
            String label
    ) {
        if (!enabled) {
            return;
        }
        if (latitude == null || longitude == null) {
            throw new BusinessException(label + " latitude and longitude are required when enabled");
        }
    }

    private boolean isEnabled(RequirementLocationRequest location) {
        return location != null && Boolean.TRUE.equals(location.getEnabled());
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

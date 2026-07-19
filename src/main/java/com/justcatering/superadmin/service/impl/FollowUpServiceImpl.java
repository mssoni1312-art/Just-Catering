package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.FollowUpCreateRequest;
import com.justcatering.superadmin.dto.request.FollowUpUpdateRequest;
import com.justcatering.superadmin.dto.response.FollowUpDetailsResponse;
import com.justcatering.superadmin.dto.response.FollowUpDropdownResponse;
import com.justcatering.superadmin.dto.response.FollowUpListResponse;
import com.justcatering.superadmin.dto.response.FollowUpReminderOptionResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.FollowUp;
import com.justcatering.superadmin.entity.Lead;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.enums.FollowUpReminder;
import com.justcatering.superadmin.enums.FollowUpStatus;
import com.justcatering.superadmin.enums.FollowUpType;
import com.justcatering.superadmin.enums.LeadStage;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.FollowUpMapper;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.FollowUpRepository;
import com.justcatering.superadmin.repository.LeadRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.FollowUpService;
import com.justcatering.superadmin.specification.FollowUpSpecification;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link FollowUpService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class FollowUpServiceImpl implements FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final ClientRepository clientRepository;
    private final LeadRepository leadRepository;
    private final UserRepository userRepository;
    private final FollowUpMapper followUpMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public FollowUpDetailsResponse create(FollowUpCreateRequest request) {
        boolean scheduleNext = resolveScheduleNext(
                request.getScheduleNextFollowUp(),
                request.getNextFollowUpDate(),
                request.getNextFollowUpTime(),
                request.getReminder(),
                request.getReminderMinutes()
        );
        validateNextFollowUpSchedule(scheduleNext, request.getNextFollowUpDate());
        Integer reminderMinutes = scheduleNext
                ? resolveReminderMinutes(request.getReminder(), request.getReminderMinutes())
                : null;

        ProspectTarget prospect = resolveProspect(request.getClientUuid(), request.getLeadUuid());

        FollowUp followUp = FollowUp.builder()
                .client(prospect.client())
                .lead(prospect.lead())
                .title(request.getTitle().trim())
                .followUpType(request.getFollowUpType() != null ? request.getFollowUpType() : FollowUpType.CALL)
                .assignedUser(resolveOptionalUser(request.getAssignedUserUuid()))
                .followUpDate(request.getFollowUpDate())
                .followUpTime(request.getFollowUpTime())
                .followUpStatus(
                        request.getFollowUpStatus() != null ? request.getFollowUpStatus() : FollowUpStatus.PENDING
                )
                .expectedBudget(request.getExpectedBudget())
                .remark(normalizeOptional(request.getRemark()))
                .nextFollowUpDate(scheduleNext ? request.getNextFollowUpDate() : null)
                .nextFollowUpTime(scheduleNext ? request.getNextFollowUpTime() : null)
                .reminderMinutes(reminderMinutes)
                .status(request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        followUp = followUpRepository.save(followUp);
        log.info("Created follow-up: {}", followUp.getTitle());
        return followUpMapper.toDetails(reloadWithRelations(followUp.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FollowUpDetailsResponse update(UUID uuid, FollowUpUpdateRequest request) {
        FollowUp followUp = findOrThrow(uuid);
        boolean scheduleNext = resolveScheduleNext(
                request.getScheduleNextFollowUp(),
                request.getNextFollowUpDate(),
                request.getNextFollowUpTime(),
                request.getReminder(),
                request.getReminderMinutes()
        );
        validateNextFollowUpSchedule(scheduleNext, request.getNextFollowUpDate());
        Integer reminderMinutes = scheduleNext
                ? resolveReminderMinutes(request.getReminder(), request.getReminderMinutes())
                : null;

        ProspectTarget prospect = resolveProspect(request.getClientUuid(), request.getLeadUuid());

        followUp.setClient(prospect.client());
        followUp.setLead(prospect.lead());
        followUp.setTitle(request.getTitle().trim());
        if (request.getFollowUpType() != null) {
            followUp.setFollowUpType(request.getFollowUpType());
        }
        followUp.setAssignedUser(resolveOptionalUser(request.getAssignedUserUuid()));
        followUp.setFollowUpDate(request.getFollowUpDate());
        followUp.setFollowUpTime(request.getFollowUpTime());
        if (request.getFollowUpStatus() != null) {
            followUp.setFollowUpStatus(request.getFollowUpStatus());
        }
        followUp.setExpectedBudget(request.getExpectedBudget());
        followUp.setRemark(normalizeOptional(request.getRemark()));
        followUp.setNextFollowUpDate(scheduleNext ? request.getNextFollowUpDate() : null);
        followUp.setNextFollowUpTime(scheduleNext ? request.getNextFollowUpTime() : null);
        followUp.setReminderMinutes(reminderMinutes);
        if (request.getStatus() != null) {
            followUp.setStatus(request.getStatus());
        }

        followUpRepository.save(followUp);
        log.info("Updated follow-up: {}", followUp.getTitle());
        return followUpMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public FollowUpDetailsResponse getByUuid(UUID uuid) {
        return followUpMapper.toDetails(reloadWithRelations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        FollowUp followUp = findOrThrow(uuid);
        followUp.softDelete();
        followUpRepository.save(followUp);
        log.info("Soft-deleted follow-up: {}", followUp.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<FollowUpListResponse> search(
            String search,
            FollowUpStatus status,
            FollowUpType type,
            UUID clientUuid,
            UUID assignedUserUuid,
            LocalDate followUpFrom,
            LocalDate followUpTo,
            Pageable pageable
    ) {
        Page<FollowUp> page = followUpRepository.findAll(
                FollowUpSpecification.filter(
                        search, status, type, clientUuid, assignedUserUuid, followUpFrom, followUpTo
                ),
                pageable
        );
        return PageResponse.from(page.map(followUpMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<FollowUpDropdownResponse> dropdown() {
        return followUpRepository
                .findByDeletedFalseAndStatusOrderByFollowUpDateDescTitleAsc(EntityStatus.ACTIVE)
                .stream()
                .map(followUpMapper::toDropdown)
                .toList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<FollowUpReminderOptionResponse> reminderOptions() {
        return Arrays.stream(FollowUpReminder.values())
                .map(option -> FollowUpReminderOptionResponse.builder()
                        .code(option)
                        .minutes(option.getMinutes())
                        .label(option.getLabel())
                        .build())
                .toList();
    }

    private Lead resolveActiveLead(UUID leadUuid) {
        Lead lead = leadRepository.findByUuidAndDeletedFalse(leadUuid)
                .orElseThrow(() -> new EntityNotFoundException("Lead", leadUuid));
        if (lead.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Selected lead is not active");
        }
        if (lead.getLeadStage() == LeadStage.LOST) {
            throw new BusinessException("Cannot create follow-up for a lost lead");
        }
        return lead;
    }

    private ProspectTarget resolveProspect(UUID clientUuid, UUID leadUuid) {
        if (leadUuid != null) {
            return ProspectTarget.forLead(resolveActiveLead(leadUuid));
        }
        if (clientUuid == null) {
            throw new BusinessException("Client or lead is required", "FOLLOWUP_PROSPECT_REQUIRED");
        }

        return clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .map(client -> {
                    if (client.getStatus() != EntityStatus.ACTIVE) {
                        throw new BusinessException("Selected client is not active");
                    }
                    return ProspectTarget.forClient(client);
                })
                .orElseGet(() -> ProspectTarget.forLead(resolveActiveLead(clientUuid)));
    }

    private record ProspectTarget(Client client, Lead lead) {

        private static ProspectTarget forClient(Client client) {
            return new ProspectTarget(client, null);
        }

        private static ProspectTarget forLead(Lead lead) {
            return new ProspectTarget(null, lead);
        }
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

    private FollowUp findOrThrow(UUID uuid) {
        return followUpRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("FollowUp", uuid));
    }

    private FollowUp reloadWithRelations(UUID uuid) {
        return followUpRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("FollowUp", uuid));
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private boolean resolveScheduleNext(
            Boolean scheduleNextFollowUp,
            LocalDate nextFollowUpDate,
            LocalTime nextFollowUpTime,
            FollowUpReminder reminder,
            Integer reminderMinutes
    ) {
        if (scheduleNextFollowUp != null) {
            return scheduleNextFollowUp;
        }
        return nextFollowUpDate != null
                || nextFollowUpTime != null
                || reminder != null
                || reminderMinutes != null;
    }

    private Integer resolveReminderMinutes(FollowUpReminder reminder, Integer reminderMinutes) {
        if (reminder != null) {
            return reminder.getMinutes();
        }
        return reminderMinutes;
    }

    private void validateNextFollowUpSchedule(boolean scheduleNext, LocalDate nextFollowUpDate) {
        if (scheduleNext && nextFollowUpDate == null) {
            throw new BusinessException("Next follow-up date is required when scheduling next follow-up");
        }
    }
}

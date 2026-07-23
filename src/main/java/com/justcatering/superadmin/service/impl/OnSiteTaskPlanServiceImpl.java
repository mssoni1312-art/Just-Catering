package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.dto.request.OnSiteTaskPlanDayRequest;
import com.justcatering.superadmin.dto.request.OnSiteTaskPlanPrepareRequest;
import com.justcatering.superadmin.dto.request.OnSiteTaskPlanSaveRequest;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanDayResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanDetailsResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanListResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanPrepareResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.entity.Client;
import com.justcatering.superadmin.entity.DepartmentMember;
import com.justcatering.superadmin.entity.OnSiteTaskPlan;
import com.justcatering.superadmin.entity.OnSiteTaskPlanDay;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.OnSiteTaskPlanMapper;
import com.justcatering.superadmin.mapper.UserMapper;
import com.justcatering.superadmin.repository.ClientRepository;
import com.justcatering.superadmin.repository.DepartmentMemberRepository;
import com.justcatering.superadmin.repository.OnSiteTaskPlanRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.service.OnSiteTaskPlanService;
import com.justcatering.superadmin.specification.OnSiteTaskPlanSpecification;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Default implementation of {@link OnSiteTaskPlanService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OnSiteTaskPlanServiceImpl implements OnSiteTaskPlanService {

    private final OnSiteTaskPlanRepository taskPlanRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final DepartmentMemberRepository departmentMemberRepository;
    private final OnSiteTaskPlanMapper taskPlanMapper;
    private final UserMapper userMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OnSiteTaskPlanPrepareResponse prepare(OnSiteTaskPlanPrepareRequest request) {
        User assignedUser = resolveActiveUser(request.getAssignedUserUuid(), "Assigned member");
        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();

        List<OnSiteTaskPlanDayResponse> days = new ArrayList<>();
        for (int dayNumber = 1; dayNumber <= request.getNumberOfDays(); dayNumber++) {
            days.add(OnSiteTaskPlanDayResponse.builder()
                    .dayNumber(dayNumber)
                    .date(startDate.plusDays(dayNumber - 1L))
                    .label(dayLabel(dayNumber))
                    .taskDescription(null)
                    .build());
        }

        return OnSiteTaskPlanPrepareResponse.builder()
                .assignedUser(userMapper.toDropdown(assignedUser))
                .numberOfDays(request.getNumberOfDays())
                .days(days)
                .build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OnSiteTaskPlanDetailsResponse create(OnSiteTaskPlanSaveRequest request) {
        validateDays(request.getDays());

        OnSiteTaskPlan plan = OnSiteTaskPlan.builder()
                .client(resolveOptionalClient(request.getClientUuid()))
                .manager(resolveActiveUser(request.getManagerUuid(), "Manager"))
                .additionalNotes(normalizeOptional(request.getAdditionalNotes()))
                .numberOfDays(request.getDays().size())
                .days(new ArrayList<>())
                .status(EntityStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        plan.getDays().addAll(buildDays(plan, request.getDays()));
        plan = taskPlanRepository.save(plan);
        log.info("Created on-site task plan {}", plan.getUuid());
        return toDetails(plan.getUuid());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OnSiteTaskPlanDetailsResponse update(UUID uuid, OnSiteTaskPlanSaveRequest request) {
        validateDays(request.getDays());

        OnSiteTaskPlan plan = taskPlanRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("On-site task plan", uuid));

        plan.setClient(resolveOptionalClient(request.getClientUuid()));
        plan.setManager(resolveActiveUser(request.getManagerUuid(), "Manager"));
        plan.setAdditionalNotes(normalizeOptional(request.getAdditionalNotes()));
        plan.setNumberOfDays(request.getDays().size());
        plan.setStatus(EntityStatus.ACTIVE);

        plan.getDays().clear();
        plan.getDays().addAll(buildDays(plan, request.getDays()));

        taskPlanRepository.save(plan);
        log.info("Updated on-site task plan {}", uuid);
        return toDetails(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public OnSiteTaskPlanDetailsResponse getByUuid(UUID uuid) {
        return toDetails(uuid);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<OnSiteTaskPlanListResponse> search(
            String search,
            UUID clientUuid,
            UUID managerUuid,
            Pageable pageable
    ) {
        Page<OnSiteTaskPlan> page = taskPlanRepository.findAll(
                OnSiteTaskPlanSpecification.filter(search, clientUuid, managerUuid),
                pageable
        );
        return PageResponse.from(page.map(plan -> {
            // Ensure lazy day collection is available for start/end dates
            if (plan.getDays() != null) {
                plan.getDays().size();
            }
            return taskPlanMapper.toList(plan, resolveDesignation(plan.getManager()));
        }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        OnSiteTaskPlan plan = taskPlanRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("On-site task plan", uuid));
        plan.softDelete();
        if (plan.getDays() != null) {
            plan.getDays().forEach(OnSiteTaskPlanDay::softDelete);
        }
        taskPlanRepository.save(plan);
        log.info("Deleted on-site task plan {}", uuid);
    }

    private OnSiteTaskPlanDetailsResponse toDetails(UUID uuid) {
        OnSiteTaskPlan plan = taskPlanRepository.findByUuidWithRelations(uuid)
                .orElseThrow(() -> new EntityNotFoundException("On-site task plan", uuid));
        plan.getDays().size();
        return taskPlanMapper.toDetails(plan, resolveDesignation(plan.getManager()));
    }

    private String resolveDesignation(User manager) {
        if (manager == null || manager.getId() == null) {
            return null;
        }
        List<DepartmentMember> memberships = departmentMemberRepository.findByUserIdAndDeletedFalse(manager.getId());
        return memberships.stream()
                .filter(member -> Boolean.TRUE.equals(member.getLead()))
                .map(DepartmentMember::getDesignation)
                .findFirst()
                .or(() -> memberships.stream().map(DepartmentMember::getDesignation).findFirst())
                .orElse(null);
    }

    private void validateDays(List<OnSiteTaskPlanDayRequest> days) {
        if (days == null || days.isEmpty()) {
            throw new BusinessException("At least one day task is required");
        }
        Set<Integer> dayNumbers = new HashSet<>();
        for (OnSiteTaskPlanDayRequest day : days) {
            if (!dayNumbers.add(day.getDayNumber())) {
                throw new BusinessException("Duplicate day number: " + day.getDayNumber());
            }
        }
    }

    private List<OnSiteTaskPlanDay> buildDays(OnSiteTaskPlan plan, List<OnSiteTaskPlanDayRequest> dayRequests) {
        List<OnSiteTaskPlanDay> days = new ArrayList<>();
        for (OnSiteTaskPlanDayRequest dayRequest : dayRequests) {
            days.add(OnSiteTaskPlanDay.builder()
                    .taskPlan(plan)
                    .dayNumber(dayRequest.getDayNumber())
                    .taskDate(dayRequest.getDate())
                    .taskDescription(dayRequest.getTaskDescription().trim())
                    .status(EntityStatus.ACTIVE)
                    .deleted(Boolean.FALSE)
                    .build());
        }
        return days;
    }

    private Client resolveOptionalClient(UUID clientUuid) {
        if (clientUuid == null) {
            return null;
        }
        Client client = clientRepository.findByUuidAndDeletedFalse(clientUuid)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientUuid));
        if (client.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException("Client is not active");
        }
        return client;
    }

    private User resolveActiveUser(UUID userUuid, String label) {
        User user = userRepository.findByUuidAndDeletedFalse(userUuid)
                .orElseThrow(() -> new EntityNotFoundException(label, userUuid));
        if (user.getStatus() != EntityStatus.ACTIVE) {
            throw new BusinessException(label + " is not active");
        }
        return user;
    }

    private String normalizeOptional(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return value.trim();
    }

    private String dayLabel(int dayNumber) {
        return switch (dayNumber) {
            case 1 -> "First Date";
            case 2 -> "Second Date";
            case 3 -> "Third Date";
            case 4 -> "Fourth Date";
            case 5 -> "Fifth Date";
            default -> "Day " + dayNumber + " Date";
        };
    }
}

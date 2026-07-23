package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.OnSiteTaskPlanDayResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanDetailsResponse;
import com.justcatering.superadmin.dto.response.OnSiteTaskPlanListResponse;
import com.justcatering.superadmin.entity.OnSiteTaskPlan;
import com.justcatering.superadmin.entity.OnSiteTaskPlanDay;
import com.justcatering.superadmin.entity.User;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * MapStruct mapper for {@link OnSiteTaskPlan} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class})
public abstract class OnSiteTaskPlanMapper {

    @Autowired
    protected ClientMapper clientMapper;

    @Autowired
    protected UserMapper userMapper;

    /**
     * Maps a task plan to a list/card response.
     *
     * @param plan               task plan with relations loaded
     * @param managerDesignation optional designation label
     * @return list response
     */
    public OnSiteTaskPlanListResponse toList(OnSiteTaskPlan plan, String managerDesignation) {
        if (plan == null) {
            return null;
        }

        LocalDate startDate = resolveStartDate(plan);
        LocalDate endDate = resolveEndDate(plan);
        User manager = plan.getManager();
        Integer numberOfDays = plan.getNumberOfDays();

        return OnSiteTaskPlanListResponse.builder()
                .uuid(plan.getUuid())
                .planDate(startDate)
                .startDate(startDate)
                .endDate(endDate)
                .clientUuid(plan.getClient() != null ? plan.getClient().getUuid() : null)
                .clientName(plan.getClient() != null ? plan.getClient().getClientName() : null)
                .managerUuid(manager != null ? manager.getUuid() : null)
                .managerName(manager != null ? manager.getFullName() : null)
                .managerEmail(manager != null ? manager.getEmail() : null)
                .managerInitials(toInitials(manager))
                .managerDesignation(managerDesignation)
                .numberOfDays(numberOfDays)
                .planLabel(planLabel(numberOfDays))
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .build();
    }

    /**
     * Maps a task plan to details.
     *
     * @param plan               task plan with relations loaded
     * @param managerDesignation optional designation label
     * @return details response
     */
    public OnSiteTaskPlanDetailsResponse toDetails(OnSiteTaskPlan plan, String managerDesignation) {
        if (plan == null) {
            return null;
        }
        return OnSiteTaskPlanDetailsResponse.builder()
                .uuid(plan.getUuid())
                .client(plan.getClient() != null ? clientMapper.toDropdown(plan.getClient()) : null)
                .manager(userMapper.toDropdown(plan.getManager()))
                .managerDesignation(managerDesignation)
                .numberOfDays(plan.getNumberOfDays())
                .additionalNotes(plan.getAdditionalNotes())
                .days(toDays(plan))
                .status(plan.getStatus())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }

    /**
     * Maps day entities.
     *
     * @param plan task plan
     * @return day responses
     */
    protected List<OnSiteTaskPlanDayResponse> toDays(OnSiteTaskPlan plan) {
        if (plan.getDays() == null) {
            return List.of();
        }
        return plan.getDays().stream()
                .filter(day -> !Boolean.TRUE.equals(day.getDeleted()))
                .sorted(Comparator.comparing(OnSiteTaskPlanDay::getDayNumber))
                .map(this::toDay)
                .toList();
    }

    /**
     * Maps a day entity.
     *
     * @param day day entity
     * @return day response
     */
    protected OnSiteTaskPlanDayResponse toDay(OnSiteTaskPlanDay day) {
        return OnSiteTaskPlanDayResponse.builder()
                .uuid(day.getUuid())
                .dayNumber(day.getDayNumber())
                .date(day.getTaskDate())
                .label(dayLabel(day.getDayNumber()))
                .taskDescription(day.getTaskDescription())
                .build();
    }

    /**
     * Builds an ordinal day label such as {@code First Date}.
     *
     * @param dayNumber day number
     * @return label
     */
    public String dayLabel(int dayNumber) {
        return switch (dayNumber) {
            case 1 -> "First Date";
            case 2 -> "Second Date";
            case 3 -> "Third Date";
            case 4 -> "Fourth Date";
            case 5 -> "Fifth Date";
            default -> "Day " + dayNumber + " Date";
        };
    }

    /**
     * Builds the card badge label.
     *
     * @param numberOfDays day count
     * @return badge text
     */
    public String planLabel(Integer numberOfDays) {
        if (numberOfDays == null || numberOfDays < 1) {
            return "Task Plan";
        }
        return numberOfDays + "-Day Task Plan";
    }

    /**
     * Builds avatar initials from the manager name.
     *
     * @param manager manager user
     * @return initials
     */
    public String toInitials(User manager) {
        if (manager == null) {
            return null;
        }
        String first = StringUtils.hasText(manager.getFirstName())
                ? manager.getFirstName().trim().substring(0, 1)
                : "";
        String last = StringUtils.hasText(manager.getLastName())
                ? manager.getLastName().trim().substring(0, 1)
                : "";
        String initials = (first + last).toUpperCase(Locale.ROOT);
        return StringUtils.hasText(initials) ? initials : null;
    }

    /**
     * Resolves the earliest task date.
     *
     * @param plan task plan
     * @return start date
     */
    protected LocalDate resolveStartDate(OnSiteTaskPlan plan) {
        return activeDays(plan).stream()
                .map(OnSiteTaskPlanDay::getTaskDate)
                .min(LocalDate::compareTo)
                .orElse(null);
    }

    /**
     * Resolves the latest task date.
     *
     * @param plan task plan
     * @return end date
     */
    protected LocalDate resolveEndDate(OnSiteTaskPlan plan) {
        return activeDays(plan).stream()
                .map(OnSiteTaskPlanDay::getTaskDate)
                .max(LocalDate::compareTo)
                .orElse(null);
    }

    private List<OnSiteTaskPlanDay> activeDays(OnSiteTaskPlan plan) {
        if (plan.getDays() == null) {
            return List.of();
        }
        return plan.getDays().stream()
                .filter(day -> !Boolean.TRUE.equals(day.getDeleted()))
                .toList();
    }
}

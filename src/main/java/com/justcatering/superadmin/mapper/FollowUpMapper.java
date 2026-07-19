package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.FollowUpDetailsResponse;
import com.justcatering.superadmin.dto.response.FollowUpDropdownResponse;
import com.justcatering.superadmin.dto.response.FollowUpListResponse;
import com.justcatering.superadmin.entity.FollowUp;
import com.justcatering.superadmin.enums.FollowUpReminder;
import com.justcatering.superadmin.mapper.LeadMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * MapStruct mapper for {@link FollowUp} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class, LeadMapper.class})
public interface FollowUpMapper {

    /**
     * Maps a follow-up to a list response.
     *
     * @param followUp follow-up entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "leadUuid", source = "lead.uuid")
    @Mapping(
            target = "leadName",
            expression = "java(followUp.getLead() != null ? followUp.getLead().getCompanyName() : null)"
    )
    @Mapping(target = "assignedUserUuid", source = "assignedUser.uuid")
    @Mapping(
            target = "assignedUserName",
            expression = "java(followUp.getAssignedUser() != null ? followUp.getAssignedUser().getFullName() : null)"
    )
    FollowUpListResponse toList(FollowUp followUp);

    /**
     * Maps a follow-up to a details response.
     *
     * @param followUp follow-up entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "lead", source = "lead")
    @Mapping(target = "assignedUser", source = "assignedUser")
    @Mapping(target = "scheduleNextFollowUp", ignore = true)
    @Mapping(target = "reminder", ignore = true)
    FollowUpDetailsResponse toDetails(FollowUp followUp);

    /**
     * Derives schedule toggle and reminder preset for details response.
     *
     * @param followUp follow-up entity
     * @param response mapped response
     */
    @AfterMapping
    default void enrichDetails(FollowUp followUp, @MappingTarget FollowUpDetailsResponse response) {
        response.setScheduleNextFollowUp(followUp.getNextFollowUpDate() != null);
        response.setReminder(resolveReminder(followUp.getReminderMinutes()));
    }

    /**
     * Maps stored minutes to a known reminder preset when possible.
     *
     * @param reminderMinutes stored minutes
     * @return reminder preset or null
     */
    default FollowUpReminder resolveReminder(Integer reminderMinutes) {
        if (reminderMinutes == null) {
            return null;
        }
        for (FollowUpReminder option : FollowUpReminder.values()) {
            if (option.getMinutes() == reminderMinutes) {
                return option;
            }
        }
        return null;
    }

    /**
     * Maps a follow-up to a dropdown response.
     *
     * @param followUp follow-up entity
     * @return dropdown response
     */
    @Mapping(
            target = "clientName",
            expression = "java(followUp.getClient() != null ? followUp.getClient().getClientName() : (followUp.getLead() != null ? followUp.getLead().getCompanyName() : null))"
    )
    FollowUpDropdownResponse toDropdown(FollowUp followUp);
}

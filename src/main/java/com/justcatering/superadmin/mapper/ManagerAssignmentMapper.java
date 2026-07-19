package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ManagerAssignmentDetailsResponse;
import com.justcatering.superadmin.dto.response.ManagerAssignmentListResponse;
import com.justcatering.superadmin.entity.ClientManagerAssignment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link ClientManagerAssignment} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, UserMapper.class, DepartmentMapper.class})
public interface ManagerAssignmentMapper {

    /**
     * Maps an assignment to a list response.
     *
     * @param assignment assignment entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(
            target = "userName",
            expression = "java(assignment.getUser() != null ? assignment.getUser().getFullName() : null)"
    )
    @Mapping(target = "departmentUuid", source = "department.uuid")
    @Mapping(target = "departmentName", source = "department.name")
    ManagerAssignmentListResponse toList(ClientManagerAssignment assignment);

    /**
     * Maps an assignment to a details response.
     *
     * @param assignment assignment entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "department", source = "department")
    ManagerAssignmentDetailsResponse toDetails(ClientManagerAssignment assignment);
}

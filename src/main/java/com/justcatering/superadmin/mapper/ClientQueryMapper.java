package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ClientQueryDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientQueryDropdownResponse;
import com.justcatering.superadmin.dto.response.ClientQueryListResponse;
import com.justcatering.superadmin.entity.ClientQuery;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
    @Mapping(target = "assignedUserUuid", source = "assignedUser.uuid")
    @Mapping(
            target = "assignedUserName",
            expression = "java(clientQuery.getAssignedUser() != null ? clientQuery.getAssignedUser().getFullName() : null)"
    )
    @Mapping(target = "departmentUuid", source = "department.uuid")
    @Mapping(target = "departmentName", source = "department.name")
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
    ClientQueryDetailsResponse toDetails(ClientQuery clientQuery);

    /**
     * Maps a client query to a dropdown response.
     *
     * @param clientQuery client query entity
     * @return dropdown response
     */
    @Mapping(target = "clientName", source = "client.clientName")
    ClientQueryDropdownResponse toDropdown(ClientQuery clientQuery);
}

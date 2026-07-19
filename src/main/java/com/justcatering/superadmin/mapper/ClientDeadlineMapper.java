package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.ClientDeadlineDetailsResponse;
import com.justcatering.superadmin.dto.response.ClientDeadlineListResponse;
import com.justcatering.superadmin.entity.ClientDeadline;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for {@link ClientDeadline} projections.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class, DepartmentMapper.class})
public interface ClientDeadlineMapper {

    /**
     * Maps a deadline record to a list response.
     *
     * @param deadline deadline entity with relations loaded
     * @return list response
     */
    @Mapping(target = "clientUuid", source = "client.uuid")
    @Mapping(target = "clientName", source = "client.clientName")
    @Mapping(target = "departmentUuid", source = "department.uuid")
    @Mapping(target = "departmentName", source = "department.name")
    ClientDeadlineListResponse toList(ClientDeadline deadline);

    /**
     * Maps a deadline record to a details response.
     *
     * @param deadline deadline entity with relations loaded
     * @return details response
     */
    @Mapping(target = "client", source = "client")
    @Mapping(target = "department", source = "department")
    ClientDeadlineDetailsResponse toDetails(ClientDeadline deadline);
}

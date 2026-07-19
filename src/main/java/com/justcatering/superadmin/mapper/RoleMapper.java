package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.PermissionListResponse;
import com.justcatering.superadmin.dto.response.RoleDetailsResponse;
import com.justcatering.superadmin.dto.response.RoleDropdownResponse;
import com.justcatering.superadmin.dto.response.RoleListResponse;
import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.entity.Role;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for {@link Role} projections.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {

    /**
     * Maps a role entity to a list response.
     *
     * @param role role entity
     * @return list response
     */
    @Mapping(target = "permissionCount", source = "role", qualifiedByName = "mapPermissionCount")
    RoleListResponse toList(Role role);

    /**
     * Maps a role entity to a details response.
     *
     * @param role role entity with permissions loaded
     * @return details response
     */
    @Mapping(target = "permissions", source = "role", qualifiedByName = "mapPermissions")
    RoleDetailsResponse toDetails(Role role);

    /**
     * Maps a role entity to a dropdown response.
     *
     * @param role role entity
     * @return dropdown response
     */
    RoleDropdownResponse toDropdown(Role role);

    /**
     * Counts active permissions on a role.
     *
     * @param role role entity
     * @return permission count
     */
    @Named("mapPermissionCount")
    default int mapPermissionCount(Role role) {
        return role.getActivePermissions().size();
    }

    /**
     * Maps active permissions to list responses.
     *
     * @param role role entity
     * @return permission list responses
     */
    @Named("mapPermissions")
    default Set<PermissionListResponse> mapPermissions(Role role) {
        return role.getActivePermissions().stream()
                .map(permission -> PermissionListResponse.builder()
                        .uuid(permission.getUuid())
                        .name(permission.getName())
                        .code(permission.getCode())
                        .module(permission.getModule())
                        .description(permission.getDescription())
                        .status(permission.getStatus())
                        .build())
                .collect(Collectors.toSet());
    }
}

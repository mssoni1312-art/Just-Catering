package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.PermissionDropdownResponse;
import com.justcatering.superadmin.dto.response.PermissionListResponse;
import com.justcatering.superadmin.entity.Permission;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for {@link Permission} projections.
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {

    /**
     * Maps a permission entity to a list response.
     *
     * @param permission permission entity
     * @return list response
     */
    PermissionListResponse toList(Permission permission);

    /**
     * Maps a permission entity to a dropdown response.
     *
     * @param permission permission entity
     * @return dropdown response
     */
    PermissionDropdownResponse toDropdown(Permission permission);
}

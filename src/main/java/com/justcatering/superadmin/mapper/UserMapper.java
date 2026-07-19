package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.PermissionListResponse;
import com.justcatering.superadmin.dto.response.RoleDropdownResponse;
import com.justcatering.superadmin.dto.response.UserDetailsResponse;
import com.justcatering.superadmin.dto.response.UserDropdownResponse;
import com.justcatering.superadmin.dto.response.UserListResponse;
import com.justcatering.superadmin.dto.response.UserSummaryResponse;
import com.justcatering.superadmin.entity.Permission;
import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.entity.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for {@link User} projections.
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface UserMapper {

    /**
     * Maps a user entity to an auth summary response.
     *
     * @param user user entity with associations loaded
     * @return summary response
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoleCodes")
    @Mapping(target = "permissions", source = "user", qualifiedByName = "mapPermissionCodes")
    UserSummaryResponse toSummary(User user);

    /**
     * Maps a user entity to a list response.
     *
     * @param user user entity
     * @return list response
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoleCodes")
    UserListResponse toList(User user);

    /**
     * Maps a user entity to a details response.
     *
     * @param user user entity with associations loaded
     * @return details response
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    @Mapping(target = "roles", source = "user", qualifiedByName = "mapRoleDropdowns")
    @Mapping(target = "permissions", source = "user", qualifiedByName = "mapPermissionCodes")
    UserDetailsResponse toDetails(User user);

    /**
     * Maps a user entity to a dropdown response.
     *
     * @param user user entity
     * @return dropdown response
     */
    @Mapping(target = "fullName", expression = "java(user.getFullName())")
    UserDropdownResponse toDropdown(User user);

    /**
     * Extracts active role codes.
     *
     * @param user user entity
     * @return role codes
     */
    @Named("mapRoleCodes")
    default Set<String> mapRoleCodes(User user) {
        return user.getActiveRoles().stream()
                .map(Role::getCode)
                .collect(Collectors.toSet());
    }

    /**
     * Extracts active permission codes.
     *
     * @param user user entity
     * @return permission codes
     */
    @Named("mapPermissionCodes")
    default Set<String> mapPermissionCodes(User user) {
        return user.getActiveRoles().stream()
                .flatMap(role -> role.getActivePermissions().stream())
                .map(Permission::getCode)
                .collect(Collectors.toSet());
    }

    /**
     * Maps active roles to dropdown responses.
     *
     * @param user user entity
     * @return role dropdowns
     */
    @Named("mapRoleDropdowns")
    default Set<RoleDropdownResponse> mapRoleDropdowns(User user) {
        return user.getActiveRoles().stream()
                .map(role -> RoleDropdownResponse.builder()
                        .uuid(role.getUuid())
                        .name(role.getName())
                        .code(role.getCode())
                        .build())
                .collect(Collectors.toSet());
    }
}

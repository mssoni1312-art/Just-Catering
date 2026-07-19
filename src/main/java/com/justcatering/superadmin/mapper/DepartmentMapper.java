package com.justcatering.superadmin.mapper;

import com.justcatering.superadmin.dto.response.DepartmentDetailsResponse;
import com.justcatering.superadmin.dto.response.DepartmentDropdownResponse;
import com.justcatering.superadmin.dto.response.DepartmentListResponse;
import com.justcatering.superadmin.dto.response.DepartmentMemberResponse;
import com.justcatering.superadmin.entity.Department;
import com.justcatering.superadmin.entity.DepartmentMember;
import java.util.Comparator;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * MapStruct mapper for {@link Department} projections.
 */
@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    /**
     * Maps a department to a list response.
     *
     * @param department department entity
     * @return list response
     */
    @Mapping(target = "memberCount", ignore = true)
    @Mapping(target = "parentName", source = "department", qualifiedByName = "mapParentName")
    DepartmentListResponse toList(Department department);

    /**
     * Maps a department to a details response.
     *
     * @param department department with members loaded
     * @return details response
     */
    @Mapping(target = "parent", source = "parent")
    @Mapping(target = "members", source = "department", qualifiedByName = "mapMembers")
    DepartmentDetailsResponse toDetails(Department department);

    /**
     * Maps a department to a dropdown response.
     *
     * @param department department entity
     * @return dropdown response
     */
    DepartmentDropdownResponse toDropdown(Department department);

    /**
     * Maps a membership to a member response.
     *
     * @param member membership entity
     * @return member response
     */
    @Mapping(target = "userUuid", source = "user.uuid")
    @Mapping(target = "fullName", expression = "java(member.getUser().getFullName())")
    @Mapping(target = "email", source = "user.email")
    DepartmentMemberResponse toMemberResponse(DepartmentMember member);

    /**
     * Resolves parent department name.
     *
     * @param department department entity
     * @return parent name or null
     */
    @Named("mapParentName")
    default String mapParentName(Department department) {
        return department.getParent() != null && !department.getParent().isDeleted()
                ? department.getParent().getName()
                : null;
    }

    /**
     * Maps active members sorted by name.
     *
     * @param department department entity
     * @return member responses
     */
    @Named("mapMembers")
    default List<DepartmentMemberResponse> mapMembers(Department department) {
        return department.getActiveMembers().stream()
                .sorted(Comparator.comparing(m -> m.getUser().getFullName(), String.CASE_INSENSITIVE_ORDER))
                .map(this::toMemberResponse)
                .toList();
    }
}

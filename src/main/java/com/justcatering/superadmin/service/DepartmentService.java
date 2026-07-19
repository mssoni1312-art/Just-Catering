package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.DepartmentCreateRequest;
import com.justcatering.superadmin.dto.request.DepartmentMemberRequest;
import com.justcatering.superadmin.dto.request.DepartmentUpdateRequest;
import com.justcatering.superadmin.dto.response.DepartmentDetailsResponse;
import com.justcatering.superadmin.dto.response.DepartmentDropdownResponse;
import com.justcatering.superadmin.dto.response.DepartmentListResponse;
import com.justcatering.superadmin.dto.response.DepartmentMemberResponse;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * Department and team-member management service contract.
 */
public interface DepartmentService {

    /**
     * Creates a department with optional members.
     *
     * @param request create payload
     * @return created department details
     */
    DepartmentDetailsResponse create(DepartmentCreateRequest request);

    /**
     * Updates a department and optionally replaces members.
     *
     * @param uuid    department UUID
     * @param request update payload
     * @return updated department details
     */
    DepartmentDetailsResponse update(UUID uuid, DepartmentUpdateRequest request);

    /**
     * Returns department details including members.
     *
     * @param uuid department UUID
     * @return department details
     */
    DepartmentDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a department.
     *
     * @param uuid department UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters departments.
     *
     * @param search     free-text search
     * @param status     status filter
     * @param parentUuid parent UUID filter
     * @param pageable   pagination
     * @return page of departments
     */
    PageResponse<DepartmentListResponse> search(
            String search,
            EntityStatus status,
            String parentUuid,
            Pageable pageable
    );

    /**
     * Returns active departments for dropdowns.
     *
     * @return dropdown options
     */
    List<DepartmentDropdownResponse> dropdown();

    /**
     * Adds or updates a member in a department.
     *
     * @param departmentUuid department UUID
     * @param request        member payload
     * @return member response
     */
    DepartmentMemberResponse addMember(UUID departmentUuid, DepartmentMemberRequest request);

    /**
     * Removes a member from a department.
     *
     * @param departmentUuid department UUID
     * @param userUuid       user UUID
     */
    void removeMember(UUID departmentUuid, UUID userUuid);
}

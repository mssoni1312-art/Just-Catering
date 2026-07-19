package com.justcatering.superadmin.service;

import com.justcatering.superadmin.dto.request.UserCreateRequest;
import com.justcatering.superadmin.dto.request.UserUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.UserDetailsResponse;
import com.justcatering.superadmin.dto.response.UserDropdownResponse;
import com.justcatering.superadmin.dto.response.UserListResponse;
import com.justcatering.superadmin.enums.EntityStatus;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

/**
 * User management service contract.
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param request create payload
     * @return created user details
     */
    UserDetailsResponse create(UserCreateRequest request);

    /**
     * Updates an existing user.
     *
     * @param uuid    user UUID
     * @param request update payload
     * @return updated user details
     */
    UserDetailsResponse update(UUID uuid, UserUpdateRequest request);

    /**
     * Returns user details by UUID.
     *
     * @param uuid user UUID
     * @return user details
     */
    UserDetailsResponse getByUuid(UUID uuid);

    /**
     * Soft-deletes a user.
     *
     * @param uuid user UUID
     */
    void delete(UUID uuid);

    /**
     * Searches and filters users with pagination.
     *
     * @param search      free-text search
     * @param status      status filter
     * @param roleCode    role code filter
     * @param createdFrom created-from filter
     * @param createdTo   created-to filter
     * @param pageable    pagination
     * @return page of users
     */
    PageResponse<UserListResponse> search(
            String search,
            EntityStatus status,
            String roleCode,
            Instant createdFrom,
            Instant createdTo,
            boolean excludeCurrentUser,
            Pageable pageable
    );

    /**
     * Returns active users for dropdowns.
     *
     * @return dropdown options
     */
    List<UserDropdownResponse> dropdown();
}

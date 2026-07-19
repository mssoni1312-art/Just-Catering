package com.justcatering.superadmin.service.impl;

import com.justcatering.superadmin.constants.AppConstants;
import com.justcatering.superadmin.dto.request.UserCreateRequest;
import com.justcatering.superadmin.dto.request.UserUpdateRequest;
import com.justcatering.superadmin.dto.response.PageResponse;
import com.justcatering.superadmin.dto.response.UserDetailsResponse;
import com.justcatering.superadmin.dto.response.UserDropdownResponse;
import com.justcatering.superadmin.dto.response.UserListResponse;
import com.justcatering.superadmin.entity.Role;
import com.justcatering.superadmin.entity.User;
import com.justcatering.superadmin.entity.UserRole;
import com.justcatering.superadmin.enums.EntityStatus;
import com.justcatering.superadmin.exception.BusinessException;
import com.justcatering.superadmin.exception.EntityNotFoundException;
import com.justcatering.superadmin.mapper.UserMapper;
import com.justcatering.superadmin.repository.RefreshTokenRepository;
import com.justcatering.superadmin.repository.RoleRepository;
import com.justcatering.superadmin.repository.UserRepository;
import com.justcatering.superadmin.repository.UserRoleEntityRepository;
import com.justcatering.superadmin.security.UserPrincipal;
import com.justcatering.superadmin.service.UserService;
import com.justcatering.superadmin.specification.UserSpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UserService}.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleEntityRepository userRoleEntityRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetailsResponse create(UserCreateRequest request) {
        String email = request.getEmail().trim().toLowerCase();
        if (userRepository.existsByEmailIgnoreCaseAndDeletedFalse(email)) {
            throw new BusinessException("Email already registered", "USER_EMAIL_EXISTS");
        }

        Set<Role> roles = resolveRoles(request.getRoleUuids());
        EntityStatus status = request.getStatus() != null ? request.getStatus() : EntityStatus.ACTIVE;

        User user = User.builder()
                .firstName(request.getFirstName().trim())
                .lastName(request.getLastName().trim())
                .email(email)
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(normalizeOptional(request.getPhone()))
                .companyName(normalizeOptional(request.getCompanyName()))
                .emailVerified(Boolean.FALSE)
                .failedLoginAttempts(0)
                .passwordChangedAt(Instant.now())
                .status(status)
                .deleted(Boolean.FALSE)
                .build();

        user = userRepository.save(user);
        assignRoles(user, roles);
        refreshPersistenceContext();

        log.info("Created user: {}", user.getEmail());
        return userMapper.toDetails(reloadWithAssociations(user.getUuid()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UserDetailsResponse update(UUID uuid, UserUpdateRequest request) {
        User user = findUserOrThrow(uuid);
        preventSelfDemotionOrLock(user, request.getStatus(), request.getRoleUuids());

        user.setFirstName(request.getFirstName().trim());
        user.setLastName(request.getLastName().trim());
        user.setPhone(normalizeOptional(request.getPhone()));
        user.setCompanyName(normalizeOptional(request.getCompanyName()));
        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
            if (request.getStatus() != EntityStatus.LOCKED) {
                user.setLockedUntil(null);
                user.setFailedLoginAttempts(0);
            }
        }

        userRepository.save(user);
        replaceRoles(user, resolveRoles(request.getRoleUuids()));

        if (user.getStatus() != EntityStatus.ACTIVE) {
            refreshTokenRepository.revokeAllActiveByUserId(user.getId());
        }

        refreshPersistenceContext();
        log.info("Updated user: {}", user.getEmail());
        return userMapper.toDetails(reloadWithAssociations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetailsResponse getByUuid(UUID uuid) {
        return userMapper.toDetails(reloadWithAssociations(uuid));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(UUID uuid) {
        User user = reloadWithAssociations(uuid);
        assertNotSelf(user);
        assertNotLastSuperAdmin(user);

        user.softDelete();
        userRepository.save(user);
        userRoleEntityRepository.softDeleteByUserId(user.getId());
        refreshTokenRepository.revokeAllActiveByUserId(user.getId());

        log.info("Soft-deleted user: {}", user.getEmail());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public PageResponse<UserListResponse> search(
            String search,
            EntityStatus status,
            String roleCode,
            Instant createdFrom,
            Instant createdTo,
            boolean excludeCurrentUser,
            Pageable pageable
    ) {
        Long excludeUserId = null;
        if (excludeCurrentUser) {
            UserPrincipal principal = currentPrincipalOrNull();
            if (principal != null) {
                excludeUserId = principal.getId();
            }
        }

        Page<User> page = userRepository.findAll(
                UserSpecification.filter(search, status, roleCode, createdFrom, createdTo, excludeUserId),
                pageable
        );
        return PageResponse.from(page.map(userMapper::toList));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDropdownResponse> dropdown() {
        return userRepository.findByDeletedFalseAndStatusOrderByFirstNameAscLastNameAsc(EntityStatus.ACTIVE)
                .stream()
                .map(userMapper::toDropdown)
                .toList();
    }

    private void assignRoles(User user, Set<Role> roles) {
        Map<Long, UserRole> existingByRoleId = userRoleEntityRepository.findByUserId(user.getId()).stream()
                .collect(Collectors.toMap(link -> link.getRole().getId(), link -> link, (a, b) -> a));

        List<UserRole> toSave = new ArrayList<>();
        for (Role role : roles) {
            UserRole existing = existingByRoleId.get(role.getId());
            if (existing != null) {
                existing.setDeleted(Boolean.FALSE);
                existing.setStatus(EntityStatus.ACTIVE);
                toSave.add(existing);
            } else {
                toSave.add(UserRole.builder()
                        .user(user)
                        .role(role)
                        .status(EntityStatus.ACTIVE)
                        .deleted(Boolean.FALSE)
                        .build());
            }
        }
        userRoleEntityRepository.saveAll(toSave);
    }

    private void replaceRoles(User user, Set<Role> roles) {
        userRoleEntityRepository.softDeleteByUserId(user.getId());
        assignRoles(user, roles);
    }

    private Set<Role> resolveRoles(Set<UUID> roleUuids) {
        List<Role> roles = roleRepository.findByUuidInAndDeletedFalse(roleUuids);
        if (roles.size() != roleUuids.size()) {
            throw new EntityNotFoundException("One or more roles were not found");
        }
        Set<Role> inactive = roles.stream()
                .filter(role -> role.getStatus() != EntityStatus.ACTIVE)
                .collect(Collectors.toSet());
        if (!inactive.isEmpty()) {
            throw new BusinessException("Cannot assign inactive roles");
        }
        return new HashSet<>(roles);
    }

    private User findUserOrThrow(UUID uuid) {
        return userRepository.findByUuidAndDeletedFalse(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User", uuid));
    }

    private User reloadWithAssociations(UUID uuid) {
        return userRepository.findByUuidWithRolesAndPermissions(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User", uuid));
    }

    private void assertNotSelf(User user) {
        UserPrincipal principal = currentPrincipalOrNull();
        if (principal != null && principal.getId().equals(user.getId())) {
            throw new BusinessException("You cannot delete your own account");
        }
    }

    private void preventSelfDemotionOrLock(User user, EntityStatus status, Set<UUID> roleUuids) {
        UserPrincipal principal = currentPrincipalOrNull();
        if (principal == null || !principal.getId().equals(user.getId())) {
            return;
        }
        if (status != null && status != EntityStatus.ACTIVE) {
            throw new BusinessException("You cannot deactivate or lock your own account");
        }
        Set<Role> roles = resolveRoles(roleUuids);
        boolean stillSuperAdmin = roles.stream()
                .anyMatch(role -> AppConstants.ROLE_SUPER_ADMIN.equals(role.getCode()));
        if (!stillSuperAdmin && user.getActiveRoles().stream()
                .anyMatch(role -> AppConstants.ROLE_SUPER_ADMIN.equals(role.getCode()))) {
            throw new BusinessException("You cannot remove SUPER_ADMIN from your own account");
        }
    }

    private void assertNotLastSuperAdmin(User user) {
        boolean isSuperAdmin = user.getActiveRoles().stream()
                .anyMatch(role -> AppConstants.ROLE_SUPER_ADMIN.equals(role.getCode()));
        if (!isSuperAdmin) {
            return;
        }
        long superAdminCount = userRepository.findAll(
                UserSpecification.filter(null, EntityStatus.ACTIVE, AppConstants.ROLE_SUPER_ADMIN, null, null)
        ).size();
        if (superAdminCount <= 1) {
            throw new BusinessException("Cannot delete the last SUPER_ADMIN user");
        }
    }

    private UserPrincipal currentPrincipalOrNull() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal principal) {
            return principal;
        }
        return null;
    }

    private void refreshPersistenceContext() {
        entityManager.flush();
        entityManager.clear();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}

-- =============================================================================
-- V3: Partial unique indexes for soft-deleted join tables
-- =============================================================================

CREATE UNIQUE INDEX IF NOT EXISTS uq_role_permissions_role_perm_active
    ON role_permissions (role_id, permission_id)
    WHERE deleted = 0;

CREATE UNIQUE INDEX IF NOT EXISTS uq_user_roles_user_role_active
    ON user_roles (user_id, role_id)
    WHERE deleted = 0;

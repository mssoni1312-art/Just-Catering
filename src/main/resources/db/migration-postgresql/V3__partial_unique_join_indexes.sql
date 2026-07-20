-- =============================================================================
-- V3: Partial unique indexes for soft-deleted join tables
-- Allows re-assigning roles/permissions after soft-delete.
-- =============================================================================

ALTER TABLE role_permissions DROP CONSTRAINT IF EXISTS uq_role_permissions_role_perm;
CREATE UNIQUE INDEX uq_role_permissions_role_perm_active
    ON role_permissions (role_id, permission_id)
    WHERE deleted = FALSE;

ALTER TABLE user_roles DROP CONSTRAINT IF EXISTS uq_user_roles_user_role;
CREATE UNIQUE INDEX uq_user_roles_user_role_active
    ON user_roles (user_id, role_id)
    WHERE deleted = FALSE;

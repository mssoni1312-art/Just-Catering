-- =============================================================================
-- V25: Clear all sample/business data for a fresh start
-- Keeps roles, permissions, and role_permissions intact.
-- Super Admin is recreated on next application startup via bootstrap.
-- =============================================================================

-- Clear self-referencing department hierarchy before truncate
UPDATE departments SET parent_id = NULL WHERE parent_id IS NOT NULL;

TRUNCATE TABLE
    client_deadlines,
    client_manager_assignments,
    payments,
    expenses,
    client_queries,
    follow_ups,
    leads,
    clients,
    department_members,
    departments,
    products,
    refresh_tokens,
    user_roles,
    users
RESTART IDENTITY CASCADE;

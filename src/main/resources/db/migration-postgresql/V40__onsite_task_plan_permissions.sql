-- =============================================================================
-- V40: On-site task plan permissions
-- =============================================================================

INSERT INTO permissions (name, code, module, description, status)
VALUES
    ('View On-site Task Plans', 'ONSITE_TASK_PLAN_VIEW', 'ONSITE_TASK_PLAN', 'View on-site manager task plans', 'ACTIVE'),
    ('Manage On-site Task Plans', 'ONSITE_TASK_PLAN_MANAGE', 'ONSITE_TASK_PLAN', 'Create and update on-site manager task plans', 'ACTIVE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code IN ('ONSITE_TASK_PLAN_VIEW', 'ONSITE_TASK_PLAN_MANAGE')
WHERE r.code IN ('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'MEMBER')
  AND r.deleted = FALSE
  AND p.deleted = FALSE;

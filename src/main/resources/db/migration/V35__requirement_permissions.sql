-- =============================================================================
-- V35: Requirement permissions
-- =============================================================================

INSERT INTO permissions (name, code, module, description, status)
VALUES
    ('View Requirements', 'REQUIREMENT_VIEW', 'REQUIREMENT', 'View on-site requirements', 'ACTIVE'),
    ('Manage Requirements', 'REQUIREMENT_MANAGE', 'REQUIREMENT', 'Create and update on-site requirements', 'ACTIVE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
JOIN permissions p ON p.code IN ('REQUIREMENT_VIEW', 'REQUIREMENT_MANAGE')
WHERE r.code IN ('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'MEMBER')
  AND r.deleted = 0
  AND p.deleted = 0;

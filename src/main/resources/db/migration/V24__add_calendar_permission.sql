-- =============================================================================
-- V24: Add calendar view permission
-- =============================================================================

INSERT INTO permissions (name, code, module, description, status)
VALUES ('View Calendar', 'CALENDAR_VIEW', 'CALENDAR', 'View calendar summary and daily schedule', 'ACTIVE');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('SUPER_ADMIN', 'ADMIN', 'MANAGER', 'MEMBER')
  AND p.code = 'CALENDAR_VIEW';

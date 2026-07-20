-- =============================================================================
-- V2: Seed default roles and permissions
-- Super Admin user is created by application bootstrap with BCrypt encoding.
-- =============================================================================

-- Default roles
INSERT INTO roles (name, code, description, is_system, status)
VALUES
    ('Super Admin', 'SUPER_ADMIN', 'Full system access', 1, 'ACTIVE'),
    ('Admin', 'ADMIN', 'Administrative access with limited system settings', 1, 'ACTIVE'),
    ('Manager', 'MANAGER', 'Department and project management access', 1, 'ACTIVE'),
    ('Member', 'MEMBER', 'Standard staff member access', 1, 'ACTIVE');

-- Permissions (module-based)
INSERT INTO permissions (name, code, module, description, status)
VALUES
    ('View Users', 'USER_VIEW', 'USER', 'View user list and details', 'ACTIVE'),
    ('Create User', 'USER_CREATE', 'USER', 'Create new users', 'ACTIVE'),
    ('Update User', 'USER_UPDATE', 'USER', 'Update existing users', 'ACTIVE'),
    ('Delete User', 'USER_DELETE', 'USER', 'Soft-delete users', 'ACTIVE'),

    ('View Roles', 'ROLE_VIEW', 'ROLE', 'View roles and permissions', 'ACTIVE'),
    ('Manage Roles', 'ROLE_MANAGE', 'ROLE', 'Create and update roles', 'ACTIVE'),

    ('View Clients', 'CLIENT_VIEW', 'CLIENT', 'View client list and details', 'ACTIVE'),
    ('Create Client', 'CLIENT_CREATE', 'CLIENT', 'Create clients', 'ACTIVE'),
    ('Update Client', 'CLIENT_UPDATE', 'CLIENT', 'Update clients', 'ACTIVE'),
    ('Delete Client', 'CLIENT_DELETE', 'CLIENT', 'Soft-delete clients', 'ACTIVE'),

    ('View Leads', 'LEAD_VIEW', 'LEAD', 'View meeting leads', 'ACTIVE'),
    ('Create Lead', 'LEAD_CREATE', 'LEAD', 'Create meeting leads', 'ACTIVE'),
    ('Update Lead', 'LEAD_UPDATE', 'LEAD', 'Update meeting leads', 'ACTIVE'),
    ('Delete Lead', 'LEAD_DELETE', 'LEAD', 'Soft-delete meeting leads', 'ACTIVE'),

    ('View Follow-ups', 'FOLLOWUP_VIEW', 'FOLLOWUP', 'View follow-ups', 'ACTIVE'),
    ('Manage Follow-ups', 'FOLLOWUP_MANAGE', 'FOLLOWUP', 'Create and update follow-ups', 'ACTIVE'),

    ('View Queries', 'QUERY_VIEW', 'QUERY', 'View queries and requirements', 'ACTIVE'),
    ('Manage Queries', 'QUERY_MANAGE', 'QUERY', 'Create and update queries', 'ACTIVE'),

    ('View Payments', 'PAYMENT_VIEW', 'PAYMENT', 'View payments and receipts', 'ACTIVE'),
    ('Manage Payments', 'PAYMENT_MANAGE', 'PAYMENT', 'Create and update payments', 'ACTIVE'),

    ('View Expenses', 'EXPENSE_VIEW', 'EXPENSE', 'View expenses', 'ACTIVE'),
    ('Manage Expenses', 'EXPENSE_MANAGE', 'EXPENSE', 'Create and update expenses', 'ACTIVE'),

    ('View Departments', 'DEPARTMENT_VIEW', 'DEPARTMENT', 'View departments', 'ACTIVE'),
    ('Manage Departments', 'DEPARTMENT_MANAGE', 'DEPARTMENT', 'Create and update departments', 'ACTIVE'),

    ('View Products', 'PRODUCT_VIEW', 'PRODUCT', 'View products', 'ACTIVE'),
    ('Manage Products', 'PRODUCT_MANAGE', 'PRODUCT', 'Create and update products', 'ACTIVE'),

    ('View Dashboard', 'DASHBOARD_VIEW', 'DASHBOARD', 'View dashboard overview', 'ACTIVE'),

    ('View Settings', 'SETTINGS_VIEW', 'SETTINGS', 'View system settings', 'ACTIVE'),
    ('Manage Settings', 'SETTINGS_MANAGE', 'SETTINGS', 'Update system settings', 'ACTIVE');

-- Super Admin gets all permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'SUPER_ADMIN'
  AND p.deleted = 0;

-- Admin gets all except SETTINGS_MANAGE and ROLE_MANAGE
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'ADMIN'
  AND p.code NOT IN ('SETTINGS_MANAGE', 'ROLE_MANAGE')
  AND p.deleted = 0;

-- Manager permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'MANAGER'
  AND p.code IN (
      'USER_VIEW',
      'CLIENT_VIEW', 'CLIENT_CREATE', 'CLIENT_UPDATE',
      'LEAD_VIEW', 'LEAD_CREATE', 'LEAD_UPDATE',
      'FOLLOWUP_VIEW', 'FOLLOWUP_MANAGE',
      'QUERY_VIEW', 'QUERY_MANAGE',
      'PAYMENT_VIEW', 'PAYMENT_MANAGE',
      'EXPENSE_VIEW', 'EXPENSE_MANAGE',
      'DEPARTMENT_VIEW',
      'PRODUCT_VIEW',
      'DASHBOARD_VIEW'
  )
  AND p.deleted = 0;

-- Member permissions
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code = 'MEMBER'
  AND p.code IN (
      'CLIENT_VIEW',
      'LEAD_VIEW', 'LEAD_CREATE',
      'FOLLOWUP_VIEW', 'FOLLOWUP_MANAGE',
      'QUERY_VIEW', 'QUERY_MANAGE',
      'EXPENSE_VIEW', 'EXPENSE_MANAGE',
      'DASHBOARD_VIEW'
  )
  AND p.deleted = 0;

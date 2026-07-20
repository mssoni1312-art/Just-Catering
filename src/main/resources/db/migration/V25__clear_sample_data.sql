-- =============================================================================
-- V25: Clear all sample/business data for a fresh start
-- =============================================================================

UPDATE departments SET parent_id = NULL WHERE parent_id IS NOT NULL;

DELETE FROM client_deadlines;
DELETE FROM client_manager_assignments;
DELETE FROM payments;
DELETE FROM expenses;
DELETE FROM client_queries;
DELETE FROM follow_ups;
DELETE FROM leads;
DELETE FROM clients;
DELETE FROM department_members;
DELETE FROM departments;
DELETE FROM products;
DELETE FROM refresh_tokens;
DELETE FROM user_roles;
DELETE FROM users;

DELETE FROM sqlite_sequence WHERE name IN (
    'client_deadlines',
    'client_manager_assignments',
    'payments',
    'expenses',
    'client_queries',
    'follow_ups',
    'leads',
    'clients',
    'department_members',
    'departments',
    'products',
    'refresh_tokens',
    'user_roles',
    'users'
);

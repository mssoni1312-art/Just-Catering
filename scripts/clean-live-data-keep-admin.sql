-- Keep roles, permissions, role_permissions, and admin@justcatering.com only.
-- Removes all business data and non-admin users.

BEGIN;

UPDATE departments SET parent_id = NULL WHERE parent_id IS NOT NULL;

DELETE FROM client_deadlines;
DELETE FROM client_manager_assignments;
DELETE FROM payments;
DELETE FROM expenses;
DELETE FROM client_queries;
DELETE FROM follow_ups;
DELETE FROM leads;
DELETE FROM clients;
DELETE FROM product_names;
DELETE FROM product_types;
DELETE FROM department_members;
DELETE FROM departments;
DELETE FROM products;
DELETE FROM refresh_tokens;

DELETE FROM user_roles
WHERE user_id IN (
    SELECT id FROM users WHERE LOWER(email) <> LOWER('admin@justcatering.com')
);

DELETE FROM users
WHERE LOWER(email) <> LOWER('admin@justcatering.com');

COMMIT;

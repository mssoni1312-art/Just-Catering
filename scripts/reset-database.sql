-- Manual database reset script
-- Keeps roles/permissions. Super Admin is recreated on next app startup.
-- Usage:
--   psql -h localhost -U just_catering -d just_catering_superadmin -f scripts/reset-database.sql

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

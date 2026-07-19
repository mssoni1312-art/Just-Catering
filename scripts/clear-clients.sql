-- Remove all client-related data only (keeps users, departments, products, leads, etc.)
-- Usage:
--   docker exec -i just-catering-postgres psql -U just_catering -d just_catering_superadmin < scripts/clear-clients.sql

BEGIN;

DELETE FROM expenses WHERE client_id IS NOT NULL;

TRUNCATE TABLE
    client_deadlines,
    client_manager_assignments,
    payments,
    client_queries,
    follow_ups,
    clients
RESTART IDENTITY CASCADE;

COMMIT;

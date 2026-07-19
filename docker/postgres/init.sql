-- =============================================================================
-- Just Catering SuperAdmin - PostgreSQL initialization
-- Runs once on first container start.
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Application schema (Flyway owns table DDL; this ensures extensions exist)
COMMENT ON DATABASE just_catering_superadmin IS 'Just Catering SuperAdmin production database';

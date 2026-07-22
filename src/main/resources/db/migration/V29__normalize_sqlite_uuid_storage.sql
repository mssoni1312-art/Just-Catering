-- =============================================================================
-- V29: Normalize SQLite UUID columns to binary storage
-- Hibernate expects 16-byte UUID values for SQLite; TEXT UUIDs were returned to
-- clients as hex-encoded strings and broke role assignment from mobile apps.
-- =============================================================================

UPDATE roles
SET uuid = unhex(replace(uuid, '-', ''))
WHERE typeof(uuid) = 'text';

UPDATE permissions
SET uuid = unhex(replace(uuid, '-', ''))
WHERE typeof(uuid) = 'text';

UPDATE role_permissions
SET uuid = unhex(replace(uuid, '-', ''))
WHERE typeof(uuid) = 'text';

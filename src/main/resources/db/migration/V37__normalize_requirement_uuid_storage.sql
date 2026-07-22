-- =============================================================================
-- V37: Normalize requirement UUID columns to binary storage (SQLite)
-- Requirements were added after V29 and still stored TEXT UUIDs, which Hibernate
-- exposed to clients as corrupted identifiers and broke delete/detail lookups.
-- =============================================================================

UPDATE requirements
SET uuid = unhex(replace(uuid, '-', ''))
WHERE typeof(uuid) = 'text';

UPDATE requirement_documents
SET uuid = unhex(replace(uuid, '-', ''))
WHERE typeof(uuid) = 'text';

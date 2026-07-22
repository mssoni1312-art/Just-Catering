-- =============================================================================
-- V28: Assign stable UUIDs to built-in system roles
-- Ensures consistent role UUIDs across SQLite/PostgreSQL and client apps.
-- =============================================================================

UPDATE roles SET uuid = 'b6c378f4-1ca2-4757-a619-ee594ab4dbd0'::uuid WHERE code = 'SUPER_ADMIN';
UPDATE roles SET uuid = '87866519-ea6b-43a8-9a0b-bc98adca395a'::uuid WHERE code = 'ADMIN';
UPDATE roles SET uuid = '649b290d-51b4-4721-8d98-c4ad677aad94'::uuid WHERE code = 'MANAGER';
UPDATE roles SET uuid = 'f08b87b2-5b41-4399-b6ea-409f052e2130'::uuid WHERE code = 'MEMBER';

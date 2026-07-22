-- =============================================================================
-- V38: Store voice recording file metadata on requirements
-- =============================================================================

ALTER TABLE requirements ADD COLUMN voice_file_name VARCHAR(255);
ALTER TABLE requirements ADD COLUMN voice_content_type VARCHAR(100);

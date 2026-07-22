-- =============================================================================
-- V31: Extend client queries with requirement list fields
-- =============================================================================

ALTER TABLE client_queries ADD COLUMN description TEXT;
ALTER TABLE client_queries ADD COLUMN scheduled_at TEXT;
ALTER TABLE client_queries ADD COLUMN has_check_in_out INTEGER NOT NULL DEFAULT 0;
ALTER TABLE client_queries ADD COLUMN voice_url VARCHAR(500);
ALTER TABLE client_queries ADD COLUMN voice_duration_seconds INTEGER;
ALTER TABLE client_queries ADD COLUMN document_url VARCHAR(500);
ALTER TABLE client_queries ADD COLUMN document_name VARCHAR(255);
ALTER TABLE client_queries ADD COLUMN document_size_bytes BIGINT;
ALTER TABLE client_queries ADD COLUMN document_content_type VARCHAR(100);

CREATE INDEX idx_client_queries_scheduled_at
    ON client_queries (scheduled_at)
    WHERE deleted = 0;

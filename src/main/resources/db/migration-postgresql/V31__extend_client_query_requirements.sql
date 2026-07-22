-- =============================================================================
-- V31: Extend client queries with requirement list fields
-- =============================================================================

ALTER TABLE client_queries ADD COLUMN description TEXT;
ALTER TABLE client_queries ADD COLUMN scheduled_at TIMESTAMPTZ;
ALTER TABLE client_queries ADD COLUMN has_check_in_out BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE client_queries ADD COLUMN voice_url VARCHAR(500);
ALTER TABLE client_queries ADD COLUMN voice_duration_seconds INTEGER;
ALTER TABLE client_queries ADD COLUMN document_url VARCHAR(500);
ALTER TABLE client_queries ADD COLUMN document_name VARCHAR(255);
ALTER TABLE client_queries ADD COLUMN document_size_bytes BIGINT;
ALTER TABLE client_queries ADD COLUMN document_content_type VARCHAR(100);

CREATE INDEX idx_client_queries_scheduled_at
    ON client_queries (scheduled_at)
    WHERE deleted = FALSE;

COMMENT ON COLUMN client_queries.description IS 'Requirement summary shown under the title';
COMMENT ON COLUMN client_queries.scheduled_at IS 'Requirement date and time';
COMMENT ON COLUMN client_queries.has_check_in_out IS 'Whether check-in and check-out details are available';

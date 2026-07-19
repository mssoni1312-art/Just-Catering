-- =============================================================================
-- V27: Allow follow-ups on leads before client conversion
-- =============================================================================

ALTER TABLE follow_ups
    ALTER COLUMN client_id DROP NOT NULL;

ALTER TABLE follow_ups
    ADD COLUMN lead_id BIGINT,
    ADD CONSTRAINT fk_follow_ups_lead
        FOREIGN KEY (lead_id) REFERENCES leads (id);

CREATE INDEX idx_follow_ups_lead_id ON follow_ups (lead_id) WHERE deleted = FALSE;

ALTER TABLE follow_ups
    ADD CONSTRAINT chk_follow_ups_client_or_lead CHECK (
        (client_id IS NOT NULL AND lead_id IS NULL)
        OR (client_id IS NULL AND lead_id IS NOT NULL)
    );

COMMENT ON COLUMN follow_ups.lead_id IS 'Meeting lead when follow-up is created before client conversion';

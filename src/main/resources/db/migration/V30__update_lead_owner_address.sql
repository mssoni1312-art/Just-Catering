-- =============================================================================
-- V30: Lead address and optional email
-- =============================================================================

ALTER TABLE leads ADD COLUMN address VARCHAR(500) NOT NULL DEFAULT '';

DROP INDEX IF EXISTS uq_leads_email_active;
CREATE UNIQUE INDEX uq_leads_email_active
    ON leads (LOWER(email))
    WHERE deleted = 0 AND LENGTH(TRIM(email)) > 0;

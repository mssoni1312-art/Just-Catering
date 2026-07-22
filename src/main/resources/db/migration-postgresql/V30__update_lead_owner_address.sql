-- =============================================================================
-- V30: Lead address and optional email
-- =============================================================================

ALTER TABLE leads ADD COLUMN address VARCHAR(500) NOT NULL DEFAULT '';

ALTER TABLE leads ALTER COLUMN email DROP NOT NULL;

DROP INDEX IF EXISTS uq_leads_email_active;
CREATE UNIQUE INDEX uq_leads_email_active
    ON leads (LOWER(email))
    WHERE deleted = FALSE AND email IS NOT NULL AND BTRIM(email) <> '';

COMMENT ON COLUMN leads.address IS 'Business or contact address';

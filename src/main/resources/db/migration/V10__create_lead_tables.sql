-- =============================================================================
-- V10: Meeting Lead tables
-- Module: Lead
-- =============================================================================

CREATE TABLE leads (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    first_name      VARCHAR(100)    NOT NULL,
    last_name       VARCHAR(100)    NOT NULL,
    email           VARCHAR(255)    NOT NULL,
    company_name    VARCHAR(200)    NOT NULL,
    phone           VARCHAR(20)     NOT NULL,
    state           VARCHAR(100)    NOT NULL,
    city            VARCHAR(100)    NOT NULL,
    approx_budget   NUMERIC(14, 2)  NOT NULL,
    product_id      BIGINT,
    notes           VARCHAR(1000),
    lead_stage      VARCHAR(30)     NOT NULL DEFAULT 'NEW',
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_leads_uuid UNIQUE (uuid),
    CONSTRAINT fk_leads_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT chk_leads_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_leads_stage CHECK (lead_stage IN (
        'NEW', 'CONTACTED', 'QUALIFIED', 'CONVERTED', 'LOST'
    )),
    CONSTRAINT chk_leads_approx_budget CHECK (approx_budget >= 0)
);

CREATE UNIQUE INDEX uq_leads_email_active
    ON leads (LOWER(email))
    WHERE deleted = FALSE;

CREATE INDEX idx_leads_status ON leads (status) WHERE deleted = FALSE;
CREATE INDEX idx_leads_stage ON leads (lead_stage) WHERE deleted = FALSE;
CREATE INDEX idx_leads_product_id ON leads (product_id) WHERE deleted = FALSE;
CREATE INDEX idx_leads_company_name ON leads (company_name) WHERE deleted = FALSE;
CREATE INDEX idx_leads_phone ON leads (phone) WHERE deleted = FALSE;
CREATE INDEX idx_leads_state ON leads (state) WHERE deleted = FALSE;
CREATE INDEX idx_leads_city ON leads (city) WHERE deleted = FALSE;
CREATE INDEX idx_leads_deleted ON leads (deleted);
CREATE INDEX idx_leads_created_at ON leads (created_at);

COMMENT ON TABLE leads IS 'Meeting leads / sales pipeline prospects';
COMMENT ON COLUMN leads.lead_stage IS 'Sales pipeline stage (NEW, CONTACTED, QUALIFIED, CONVERTED, LOST)';
COMMENT ON COLUMN leads.approx_budget IS 'Approximate budget indicated by the lead';

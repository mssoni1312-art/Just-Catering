-- =============================================================================
-- V10: Meeting Lead tables
-- Module: Lead
-- =============================================================================

CREATE TABLE leads (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
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
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
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
    WHERE deleted = 0;

CREATE INDEX idx_leads_status ON leads (status) WHERE deleted = 0;
CREATE INDEX idx_leads_stage ON leads (lead_stage) WHERE deleted = 0;
CREATE INDEX idx_leads_product_id ON leads (product_id) WHERE deleted = 0;
CREATE INDEX idx_leads_company_name ON leads (company_name) WHERE deleted = 0;
CREATE INDEX idx_leads_phone ON leads (phone) WHERE deleted = 0;
CREATE INDEX idx_leads_state ON leads (state) WHERE deleted = 0;
CREATE INDEX idx_leads_city ON leads (city) WHERE deleted = 0;
CREATE INDEX idx_leads_deleted ON leads (deleted);
CREATE INDEX idx_leads_created_at ON leads (created_at);

-- =============================================================================
-- V8: Client tables
-- Module: Client
-- =============================================================================

CREATE TABLE clients (
    id                                  BIGSERIAL       PRIMARY KEY,
    uuid                                UUID            NOT NULL DEFAULT gen_random_uuid(),
    client_name                         VARCHAR(200)    NOT NULL,
    contact_person                      VARCHAR(150)    NOT NULL,
    mobile                              VARCHAR(20),
    email                               VARCHAR(255)    NOT NULL,
    gst_number                          VARCHAR(20),
    client_type                         VARCHAR(100)    NOT NULL,
    product_id                          BIGINT          NOT NULL,
    deal_date                           DATE,
    total_amount                        NUMERIC(14, 2)  NOT NULL DEFAULT 0.00,
    budget                              NUMERIC(14, 2),
    notes                               VARCHAR(1000),
    client_stage                        VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    priority                            VARCHAR(20)     NOT NULL DEFAULT 'MEDIUM',
    requirements_completion_percentage  NUMERIC(5, 2)   NOT NULL DEFAULT 0.00,
    state                               VARCHAR(100),
    city                                VARCHAR(100),
    created_at                          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at                          TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by                          BIGINT,
    updated_by                          BIGINT,
    deleted                             BOOLEAN         NOT NULL DEFAULT FALSE,
    status                              VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                             BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_clients_uuid UNIQUE (uuid),
    CONSTRAINT fk_clients_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT chk_clients_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_clients_stage CHECK (client_stage IN (
        'INTERESTED', 'IN_PROGRESS', 'ACTIVE', 'ON_HOLD', 'COMPLETED', 'CHURNED'
    )),
    CONSTRAINT chk_clients_priority CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH', 'URGENT')),
    CONSTRAINT chk_clients_total_amount CHECK (total_amount >= 0),
    CONSTRAINT chk_clients_budget CHECK (budget IS NULL OR budget >= 0),
    CONSTRAINT chk_clients_requirements_pct CHECK (
        requirements_completion_percentage >= 0
        AND requirements_completion_percentage <= 100
    )
);

CREATE UNIQUE INDEX uq_clients_email_active
    ON clients (LOWER(email))
    WHERE deleted = FALSE;

CREATE INDEX idx_clients_status ON clients (status) WHERE deleted = FALSE;
CREATE INDEX idx_clients_stage ON clients (client_stage) WHERE deleted = FALSE;
CREATE INDEX idx_clients_product_id ON clients (product_id) WHERE deleted = FALSE;
CREATE INDEX idx_clients_client_name ON clients (client_name) WHERE deleted = FALSE;
CREATE INDEX idx_clients_mobile ON clients (mobile) WHERE deleted = FALSE AND mobile IS NOT NULL;
CREATE INDEX idx_clients_priority ON clients (priority) WHERE deleted = FALSE;
CREATE INDEX idx_clients_deal_date ON clients (deal_date) WHERE deleted = FALSE;
CREATE INDEX idx_clients_deleted ON clients (deleted);
CREATE INDEX idx_clients_created_at ON clients (created_at);

COMMENT ON TABLE clients IS 'CRM clients / companies';
COMMENT ON COLUMN clients.client_type IS 'Business type (e.g. Catering Services)';
COMMENT ON COLUMN clients.client_stage IS 'Sales/delivery stage of the client engagement';
COMMENT ON COLUMN clients.requirements_completion_percentage IS 'Requirements completion percentage shown on client detail';

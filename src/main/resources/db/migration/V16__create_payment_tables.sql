-- =============================================================================
-- V16: Payment / invoice / receipt tables
-- Module: Payment
-- =============================================================================

CREATE TABLE payments (
    id              INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id       BIGINT          NOT NULL,
    product_id      BIGINT,
    invoice_number  VARCHAR(50)     NOT NULL,
    payment_date    DATE            NOT NULL,
    amount          NUMERIC(14, 2)  NOT NULL,
    bank_type       VARCHAR(50),
    payment_mode    VARCHAR(30)     NOT NULL DEFAULT 'CASH',
    remarks         TEXT,
    receipt_url     VARCHAR(500),
    created_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_payments_uuid UNIQUE (uuid),
    CONSTRAINT fk_payments_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_payments_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT chk_payments_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_payments_amount CHECK (amount >= 0),
    CONSTRAINT chk_payments_mode CHECK (payment_mode IN (
        'CASH', 'UPI', 'BANK_TRANSFER', 'CARD', 'CHEQUE', 'OTHER'
    ))
);

CREATE UNIQUE INDEX uq_payments_invoice_number_active
    ON payments (invoice_number)
    WHERE deleted = 0;

CREATE INDEX idx_payments_status ON payments (status) WHERE deleted = 0;
CREATE INDEX idx_payments_client_id ON payments (client_id) WHERE deleted = 0;
CREATE INDEX idx_payments_product_id ON payments (product_id) WHERE deleted = 0;
CREATE INDEX idx_payments_payment_mode ON payments (payment_mode) WHERE deleted = 0;
CREATE INDEX idx_payments_payment_date ON payments (payment_date) WHERE deleted = 0;
CREATE INDEX idx_payments_invoice_number ON payments (invoice_number) WHERE deleted = 0;
CREATE INDEX idx_payments_deleted ON payments (deleted);
CREATE INDEX idx_payments_created_at ON payments (created_at);

-- =============================================================================
-- V16: Payment / invoice / receipt tables
-- Module: Payment
-- =============================================================================

CREATE TABLE payments (
    id              BIGSERIAL       PRIMARY KEY,
    uuid            UUID            NOT NULL DEFAULT gen_random_uuid(),
    client_id       BIGINT          NOT NULL,
    product_id      BIGINT,
    invoice_number  VARCHAR(50)     NOT NULL,
    payment_date    DATE            NOT NULL,
    amount          NUMERIC(14, 2)  NOT NULL,
    bank_type       VARCHAR(50),
    payment_mode    VARCHAR(30)     NOT NULL DEFAULT 'CASH',
    remarks         TEXT,
    receipt_url     VARCHAR(500),
    created_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         BOOLEAN         NOT NULL DEFAULT FALSE,
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
    WHERE deleted = FALSE;

CREATE INDEX idx_payments_status ON payments (status) WHERE deleted = FALSE;
CREATE INDEX idx_payments_client_id ON payments (client_id) WHERE deleted = FALSE;
CREATE INDEX idx_payments_product_id ON payments (product_id) WHERE deleted = FALSE;
CREATE INDEX idx_payments_payment_mode ON payments (payment_mode) WHERE deleted = FALSE;
CREATE INDEX idx_payments_payment_date ON payments (payment_date) WHERE deleted = FALSE;
CREATE INDEX idx_payments_invoice_number ON payments (invoice_number) WHERE deleted = FALSE;
CREATE INDEX idx_payments_deleted ON payments (deleted);
CREATE INDEX idx_payments_created_at ON payments (created_at);

COMMENT ON TABLE payments IS 'Client payment receipts / invoices';
COMMENT ON COLUMN payments.invoice_number IS 'Unique invoice or receipt number (e.g. INV-0231)';
COMMENT ON COLUMN payments.amount IS 'Paid amount for this receipt';
COMMENT ON COLUMN payments.bank_type IS 'Optional bank or channel label (e.g. GPay, NEFT, HDFC)';
COMMENT ON COLUMN payments.payment_mode IS 'Payment mode (CASH, UPI, BANK_TRANSFER, CARD, CHEQUE, OTHER)';
COMMENT ON COLUMN payments.receipt_url IS 'Optional uploaded receipt document URL';

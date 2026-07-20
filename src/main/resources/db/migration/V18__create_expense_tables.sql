-- =============================================================================
-- V18: Expense tables
-- Module: Expense
-- =============================================================================

CREATE TABLE expenses (
    id                      INTEGER       PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    client_id               BIGINT,
    title                   VARCHAR(200)    NOT NULL,
    expense_type            VARCHAR(30)     NOT NULL,
    member_user_id          BIGINT,
    expense_date            DATE            NOT NULL,
    paid_date               DATE,
    due_date                DATE,
    amount                  NUMERIC(14, 2)  NOT NULL,
    payment_mode            VARCHAR(50),
    from_city               VARCHAR(100),
    to_city                 VARCHAR(100),
    from_date               DATE,
    to_date                 DATE,
    km                      NUMERIC(10, 2),
    account_contact         VARCHAR(200),
    remarks                 VARCHAR(1000),
    bill_url                VARCHAR(500),
    created_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TEXT     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by              BIGINT,
    updated_by              BIGINT,
    deleted                 INTEGER         NOT NULL DEFAULT 0,
    status                  VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version                 BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_expenses_uuid UNIQUE (uuid),
    CONSTRAINT fk_expenses_client
        FOREIGN KEY (client_id) REFERENCES clients (id),
    CONSTRAINT fk_expenses_member_user
        FOREIGN KEY (member_user_id) REFERENCES users (id),
    CONSTRAINT chk_expenses_status CHECK (status IN ('ACTIVE', 'INACTIVE')),
    CONSTRAINT chk_expenses_type CHECK (expense_type IN (
        'TRAVEL', 'FOOD', 'OFFICE', 'OTHER'
    )),
    CONSTRAINT chk_expenses_amount CHECK (amount >= 0),
    CONSTRAINT chk_expenses_km CHECK (km IS NULL OR km >= 0)
);

CREATE INDEX idx_expenses_status ON expenses (status) WHERE deleted = 0;
CREATE INDEX idx_expenses_client_id ON expenses (client_id) WHERE deleted = 0;
CREATE INDEX idx_expenses_member_user_id ON expenses (member_user_id) WHERE deleted = 0;
CREATE INDEX idx_expenses_type ON expenses (expense_type) WHERE deleted = 0;
CREATE INDEX idx_expenses_expense_date ON expenses (expense_date) WHERE deleted = 0;
CREATE INDEX idx_expenses_paid_date ON expenses (paid_date) WHERE deleted = 0;
CREATE INDEX idx_expenses_due_date ON expenses (due_date) WHERE deleted = 0;
CREATE INDEX idx_expenses_deleted ON expenses (deleted);
CREATE INDEX idx_expenses_created_at ON expenses (created_at);

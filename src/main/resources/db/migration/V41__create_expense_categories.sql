-- =============================================================================
-- V41: Expense categories for Overall Expenses / Add Category
-- =============================================================================

CREATE TABLE expense_categories (
    id              INTEGER         PRIMARY KEY AUTOINCREMENT,
    uuid            TEXT            NOT NULL DEFAULT (lower(hex(randomblob(4))) || '-' || lower(hex(randomblob(2))) || '-4' || substr(lower(hex(randomblob(2))),2) || '-' || substr('89ab',abs(random()) % 4 + 1, 1) || substr(lower(hex(randomblob(2))),2) || '-' || lower(hex(randomblob(6)))),
    name            VARCHAR(100)    NOT NULL,
    code            VARCHAR(30),
    icon_key        VARCHAR(50),
    sort_order      INTEGER         NOT NULL DEFAULT 0,
    created_at      TEXT            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TEXT            NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by      BIGINT,
    updated_by      BIGINT,
    deleted         INTEGER         NOT NULL DEFAULT 0,
    status          VARCHAR(30)     NOT NULL DEFAULT 'ACTIVE',
    version         BIGINT          NOT NULL DEFAULT 0,

    CONSTRAINT uq_expense_categories_uuid UNIQUE (uuid),
    CONSTRAINT chk_expense_categories_status CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE UNIQUE INDEX uq_expense_categories_name_active
    ON expense_categories (LOWER(name))
    WHERE deleted = 0;

CREATE UNIQUE INDEX uq_expense_categories_code_active
    ON expense_categories (UPPER(code))
    WHERE deleted = 0 AND code IS NOT NULL;

CREATE INDEX idx_expense_categories_status ON expense_categories (status) WHERE deleted = 0;
CREATE INDEX idx_expense_categories_deleted ON expense_categories (deleted);
CREATE INDEX idx_expense_categories_sort_order ON expense_categories (sort_order) WHERE deleted = 0;

INSERT INTO expense_categories (name, code, icon_key, sort_order, status, deleted, version)
VALUES
    ('Travel Expense', 'TRAVEL', 'travel', 1, 'ACTIVE', 0, 0),
    ('Food Expense', 'FOOD', 'food', 2, 'ACTIVE', 0, 0),
    ('Office Expenses', 'OFFICE', 'office', 3, 'ACTIVE', 0, 0),
    ('Other', 'OTHER', 'other', 4, 'ACTIVE', 0, 0);
